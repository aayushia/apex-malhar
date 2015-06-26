/**
 * Copyright (C) 2015 DataTorrent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.contrib.apachelog;

import com.datatorrent.api.LocalMode;
import org.apache.hadoop.conf.Configuration;
import org.junit.Test;

/**
 *
 */
public class ApplicationLogGeneratorTest
{
  public ApplicationLogGeneratorTest()
  {
  }

  @Test
  public void testSomeMethod() throws Exception
  {
    ApplicationLogGenerator app = new ApplicationLogGenerator();
    LocalMode lma = LocalMode.newInstance();
    app.populateDAG(lma.getDAG(), new Configuration(false));

    final LocalMode.Controller lc = lma.getController();
    long start = System.currentTimeMillis();
    lc.run();
    long end = System.currentTimeMillis();
    long time = end -start;
    System.out.println("Test used "+time+" ms");
  }
}
