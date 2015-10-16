/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.tomitribe.beryllium;

import cucumber.api.java.en.Given;
import redis.clients.jedis.Jedis;

public class RedisSteps {
  private final Jedis jedis = new Jedis("localhost");

  @Given("^I have cleaned redis(?: db (\\d+))?$")
  public void iHaveCleanedRedisDb(final Integer db) {
    if (db == null) {
      jedis.flushAll();
    } else {
      jedis.select(db);
      jedis.flushDB();
    }
  }
}
