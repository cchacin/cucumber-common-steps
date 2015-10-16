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

import java.util.Map;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import redis.clients.jedis.Jedis;

import static com.google.common.truth.Truth.assertThat;

public class RedisScoredMembersSteps {
  private final Jedis jedis = new Jedis("localhost");

  @Given("^I have the redis scored member \"([^\"]*)\"(?: in the db (\\d+))? with score \"([^\"]*)\" and value \"([^\"]*)\"$")
  public void iHaveTheRedisScoredMemberInTheDbWithScoreAndValue(final String key,
                                                                final int database,
                                                                final String score,
                                                                final String value) {
    final double scoreValue = Double.parseDouble(score);
    jedis.select(database);
    jedis.zadd(key, scoreValue, value);
  }

  @Given("^I have the redis scored members \"([^\"]*)\"(?: in the db (\\d+))? with values:$")
  public void iHaveTheRedisScoredMembersInTheDbWithValuesColon(final String key,
                                                               final int database, final DataTable dataTable) {
    final Map<String, Double> table = dataTable.asMap(String.class, Double.class);
    jedis.select(database);
    jedis.zadd(key, table);
  }

  @Then("^I should have the redis scored member \"([^\"]*)\"(?: in the db (\\d+))? with score \"([^\"]*)\" and value \"([^\"]*)\"$")
  public void iShouldHaveTheRedisScoredMemberInTheDbWithScoreAndValue(
          final String key, final int database, final String score, final String value) {
    final double scoredValue = Double.parseDouble(score);
    jedis.select(database);
    assertThat(jedis.zscore(key, value)).isEqualTo(scoredValue);
  }

  @Then("^I should have the redis scored members \"([^\"]*)\"(?: in the db (\\d+))? with values:$")
  public void iShouldHaveTheRedisScoredMembersInTheDbWithValuesColon(final String key,
                                                                     final int database, final DataTable dataTable) {
    final Map<String, Double> table = dataTable.asMap(String.class, Double.class);
    jedis.select(database);
    for (Map.Entry<String, Double> entry : table.entrySet()) {
      assertThat(jedis.zscore(key, entry.getKey())).isEqualTo(entry.getValue());
    }
  }
}
