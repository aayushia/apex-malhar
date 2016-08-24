package com.example.NYCTrafficAnalysisApp;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.TimeZone;

/**
 * Created by aayushi on 7/7/16.
 */
public class POJOobject {

    private String vendor;
    private long pickup_datetime;
    private long dropoff_datetime;
    private int passenger_count;
    private double trip_distance;
    private double pickup_longitude;
    private double pickup_latitude;
    private int rate_code_id;
    private String store_and_fwd_flag;
    private double dropoff_longitude;
    private double dropoff_latitude;
    private String payment_type;
    private double fare_amount;
    private double extra;
    private double mta_tax;
    private double tip_amount;
    private double tolls_amount;
    private double improvement_surcharge;
    private double total_amount;

    @Override
    public String toString()
    {
        return "POJOobject [pickup_datetime=" + pickup_datetime + /*", dropoff_datetime=" + dropoff_datetime + /*", passenger_count=" + passenger_count +*/ ", trip_distance=" + trip_distance + /*", pickup_longitude=" + pickup_longitude + ", pickup_latitude=" + pickup_latitude +*/ ", total_amount=" + total_amount + "]";
    }

    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }

    public long getPickup_datetime()
    {
        return pickup_datetime;
    }

    public void setPickup_datetime(String pickup_datetime) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //formatter.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        Date parsedTime = formatter.parse(pickup_datetime);
        this.pickup_datetime = parsedTime.getTime();
    }

    public long getDropoff_datetime()
    {
        return dropoff_datetime;
    }

    public void setDropoff_datetime(String dropoff_datetime) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //formatter.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        Date parsedTime = formatter.parse(dropoff_datetime);
        this.dropoff_datetime = parsedTime.getTime();
    }

    public int getPassenger_count()
    {
        return passenger_count;
    }

    public void setPassenger_count(int passenger_count)
    {
        this.passenger_count = passenger_count;
    }

    public double getTrip_distance()
    {
        return trip_distance;
    }

    public void setTrip_distance(double trip_distance)
    {
        this.trip_distance = trip_distance;
    }

    public double getPickup_longitude()
    {
        return pickup_longitude;
    }

    public void setPickup_longitude(double pickup_longitude)
    {
        this.pickup_longitude = pickup_longitude;
    }

    public double getPickup_latitude()
    {
        return pickup_latitude;
    }

    public void setPickup_latitude(double pickup_latitude)
    {
        this.pickup_latitude = pickup_latitude;
    }

    public void setRate_code_id(int rate_code_id)
    {
        this.rate_code_id = rate_code_id;
    }

    public void setStore_and_fwd_flag(String store_and_fwd_flag)
    {
        this.store_and_fwd_flag = store_and_fwd_flag;
    }

    public void setDropoff_longitude(double dropoff_longitude)
    {
        this.dropoff_longitude = dropoff_longitude;
    }

    public void setDropoff_latitude(double dropoff_latitude)
    {
        this.dropoff_latitude = dropoff_latitude;
    }

    public void setPayment_type(String payment_type)
    {
        this.payment_type = payment_type;
    }

    public void setFare_amount(double fare_amount)
    {
        this.fare_amount = fare_amount;
    }

    public void setExtra(double extra)
    {
        this.extra = extra;
    }

    public void setMta_tax(double mta_tax)
    {
        this.mta_tax = mta_tax;
    }

    public void setTip_amount(double tip_amount)
    {
        this.tip_amount = tip_amount;
    }

    public void setTolls_amount(double tolls_amount)
    {
        this.tolls_amount = tolls_amount;
    }

//    public void setImprovement_surcharge(double improvement_surcharge)
//    {
//        this.improvement_surcharge = improvement_surcharge;
//    }

    public double getTotal_amount()
    {
        return total_amount;
    }

    public void setTotal_amount(double total_amount)
    {
        this.total_amount = total_amount;
    }

//    public long getTime()
//    {
//        return time = System.currentTimeMillis();
//    }

}
