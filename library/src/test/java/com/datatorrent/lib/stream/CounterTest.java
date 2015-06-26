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
package com.datatorrent.lib.stream;

import org.junit.Assert;
import org.junit.Test;

import com.datatorrent.lib.testbench.CountTestSink;

/**
 * Functional test for {@link com.datatorrent.lib.stream.Counter}<p>
 * <br>
 */
public class CounterTest {

    /**
     * Test oper pass through. The Object passed is not relevant
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testNodeProcessing() throws Exception
    {
      Counter oper = new Counter();
      CountTestSink cSink = new CountTestSink();

      oper.output.setSink(cSink);
      int numtuples = 100;

      oper.beginWindow(0);
      for (int i = 0; i < numtuples; i++) {
        oper.input.process(i);
      }
      oper.endWindow();

      oper.beginWindow(1);
      for (int i = 0; i < numtuples; i++) {
        oper.input.process(i);
      }
      oper.endWindow();

      Assert.assertEquals("number emitted tuples", 2, cSink.getCount());
    }
}
