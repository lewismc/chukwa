/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.chukwa.extraction.hbase;


import java.util.HashMap;
import org.apache.log4j.Logger;

public class ProcessorFactory {
  static Logger log = Logger.getLogger(ProcessorFactory.class);

  private static HashMap<String, AbstractProcessor> processors = new HashMap<String, AbstractProcessor>(); // registry

  public ProcessorFactory() {
  }

  public static AbstractProcessor getProcessor(String parserClass)
      throws UnknownRecordTypeException {
    if (processors.containsKey(parserClass)) {
      return processors.get(parserClass);
    } else {
      AbstractProcessor processor = null;
      try {
        processor = (AbstractProcessor) Class.forName(parserClass).getConstructor()
            .newInstance();
      } catch (ClassNotFoundException e) {
        throw new UnknownRecordTypeException("Unknown parserClass:"
            + parserClass, e);
      } catch (Exception e) {
        throw new UnknownRecordTypeException("error constructing processor", e);
      }

      // TODO using a ThreadSafe/reuse flag to actually decide if we want
      // to reuse the same processor again and again
      processors.put(parserClass, processor);
      return processor;
    }
  }
}
