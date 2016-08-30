/**
 * Put your copyright and license info here.
 */
package com.example.NYCTrafficAnalysisApp;

import org.apache.apex.malhar.lib.dimensions.DimensionsEvent.Aggregate;
import org.apache.apex.malhar.lib.dimensions.DimensionsEvent.InputEvent;
import org.apache.hadoop.conf.Configuration;
import com.datatorrent.lib.expression.JavaExpressionParser;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.DAG;
import com.datatorrent.api.Context.OperatorContext;
import com.datatorrent.lib.fileaccess.TFileImpl;
import com.datatorrent.lib.appdata.schemas.SchemaUtils;
import com.datatorrent.api.Context;
import com.datatorrent.lib.io.ConsoleOutputOperator;
import com.datatorrent.lib.dimensions.DimensionsComputationFlexibleSingleSchemaPOJO;
import com.datatorrent.contrib.dimensions.AppDataSingleSchemaDimensionStoreHDHT;
import com.datatorrent.lib.io.PubSubWebSocketAppDataQuery;
import com.datatorrent.lib.io.PubSubWebSocketAppDataResult;
import com.datatorrent.lib.statistics.DimensionsComputationUnifierImpl;

@ApplicationAnnotation(name = "NYCTrafficAnalysisApp")
public class NYCTrafficAnalysisApp implements StreamingApplication
{
  private static final transient Logger logger = LoggerFactory.getLogger(NYCTrafficAnalysisApp.class);

  public String appName = "NYCTrafficAnalysisApp";

  protected String prop_store_path, partition_count, number_of_buckets;

  public void populateDAG(DAG dag, Configuration conf)
  {
    String csvSchema = SchemaUtils.jarResourceFileToString("csvSchema.json");
    String dcSchema = SchemaUtils.jarResourceFileToString("dcSchema.json");

    LineByLineFileInputOperator reader = dag.addOperator("Reader",  LineByLineFileInputOperator.class);
    CsvParser parser = dag.addOperator("Parser", CsvParser.class);
    dag.setAttribute(parser, OperatorContext.MEMORY_MB, 2048);
    //ConsoleOutputOperator consoleOutput = dag.addOperator("Console", ConsoleOutputOperator.class);

    reader.setDirectory("/user/aayushi/datasets");
    parser.setSchema(csvSchema);

    //Dimension Computation
    DimensionsComputationFlexibleSingleSchemaPOJO dimensions = dag.addOperator("DimensionsComputation", DimensionsComputationFlexibleSingleSchemaPOJO.class);
    //Set operator properties
    dag.setAttribute(dimensions, OperatorContext.MEMORY_MB, 2048);

    //Key expression
    Map<String, String> keyToExpression = Maps.newHashMap();
    keyToExpression.put("time", "getPickup_datetime()");
    keyToExpression.put("trip_distance", "getTrip_distance()");
    //keyToExpression.put("dropoff_datetime", "getDropoff_datetime()");
    //keyToExpression.put("pickup_longitude", "getPickup_longitude()");
    //keyToExpression.put("pickup_latitude", "getPickup_latitude()");
    dimensions.setKeyToExpression(keyToExpression);

    //Aggregate expression
    Map<String, String> aggregateToExpression = Maps.newHashMap();
    aggregateToExpression.put("total_amount", "getTotal_amount()");
    //aggregateToExpression.put("trip_distance", "getTrip_distance()");
    // aggregateToExpression.put("passenger_count", "getPassenger_count()");
    dimensions.setAggregateToExpression(aggregateToExpression);

    dimensions.setConfigurationSchemaJSON(dcSchema);
    dimensions.setUnifier(new DimensionsComputationUnifierImpl<InputEvent, Aggregate>());
    //dag.setUnifierAttribute(dimensions.output, OperatorContext.MEMORY_MB, 3072);
    //dag.setUnifierAttribute(dimensions.output, Context.PortContext.UNIFIER_LIMIT, 2);

    //Dimension Store
    AppDataSingleSchemaDimensionStoreHDHT store = dag.addOperator("StoreHDHT", AppDataSingleSchemaDimensionStoreHDHT.class);
    prop_store_path = "dt.application." + appName + ".operator.StoreHDHT.fileStore.basePathPrefix";
    String basePath = Preconditions.checkNotNull(conf.get(prop_store_path),
      "base path should be specified in the properties.xml");
    TFileImpl hdsFile = new TFileImpl.DTFileImpl();
    basePath += System.currentTimeMillis();
    hdsFile.setBasePath(basePath);
    store.setFileStore(hdsFile);

    store.setConfigurationSchemaJSON(dcSchema);
    partition_count = "dt.application." + appName + ".operator.StoreHDHT.partitionCount";
    number_of_buckets = "dt.application." + appName + ".operator.StoreHDHT.numberOfBuckets";
//    store.setPartitionCount(2);
//    store.setNumberOfBuckets(2);

    //Query
    PubSubWebSocketAppDataQuery query = createAppDataQuery();
    URI queryUri = ConfigUtil.getAppDataQueryPubSubURI(dag, conf);
    logger.info("QueryUri: {}", queryUri);
    query.setUri(queryUri);
    //using the EmbeddableQueryInfoProvider instead to get rid of the problem of query schema when latency is very long
    store.setEmbeddableQueryInfoProvider(query);

    //Query Result
    PubSubWebSocketAppDataResult queryResult = createAppDataResult();
    dag.addOperator("QueryResult", queryResult);
    queryResult.setUri(queryUri);

    //Setting Port Attributes
    dag.setOutputPortAttribute(parser.out, Context.PortContext.TUPLE_CLASS, POJOobject.class);
    //dag.setInputPortAttribute(consoleOutput.input, Context.PortContext.TUPLE_CLASS, POJOobject.class);
    dag.setInputPortAttribute(dimensions.input, Context.PortContext.TUPLE_CLASS, POJOobject.class);

    //Add Streams
    dag.addStream("FileInputToParser", reader.output, parser.in);
    dag.addStream("ParserToDC", parser.out, dimensions.input);
    dag.addStream("DimensionalStreamToStore", dimensions.output, store.input);
    dag.addStream("StoreToQueryResult", store.queryResult, queryResult.input);
  }

  protected PubSubWebSocketAppDataQuery createAppDataQuery()
  {
    return new PubSubWebSocketAppDataQuery();
  }

  protected PubSubWebSocketAppDataResult createAppDataResult()
  {
    return new PubSubWebSocketAppDataResult();
  }

}
