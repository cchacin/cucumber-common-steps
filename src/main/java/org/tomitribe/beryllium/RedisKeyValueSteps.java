/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.tomitribe.beryllium;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static com.google.common.truth.Truth.assertThat;


public class RedisKeyValueSteps {
  private final JedisPool jedisPool = new JedisPool("localhost");

  private Jedis getJedis(final Integer db) {
    final Jedis jedis = jedisPool.getResource();
    if (db != null) {
      jedis.select(db);
    } else {
      jedis.select(0);
    }
    return jedis;
  }

  @Given("^I have the redis key \"([^\"]*)\"(?: in the db (\\d+))? with value \"([^\"]*)\"$")
  public void iHaveTheRedisKeyWithValue(final String key, final Integer db, final String value)
      throws Throwable {
    final Jedis jedis = getJedis(db);
    jedis.set(key, value);
    jedis.close();
  }

  @Given("^I have the redis key \"([^\"]*)\"(?: in the db (\\d+))? with value in file \"([^\"]*)\"$")
  public void iHaveTheRedisKeyInTheDbWithValueInFile(final String key, final Integer db,
                                                     final String filename) throws Throwable {
    this.iHaveTheRedisKeyWithValue(key, db, Utility.fileContent(filename));
  }

  @Given("^I have the redis key \"([^\"]*)\"(?: in the db (\\d+))? with value:$")
  public void iHaveTheRedisKeyInTheDbWithValue(final String key, final Integer db,
                                               final String value) throws Throwable {
    this.iHaveTheRedisKeyWithValue(key, db, value);
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should be \"([^\"]*)\"$")
  public void theRedisKeyInTheDbShouldBe(final String key, final Integer db, final String value)
      throws Throwable {
    final Jedis jedis = getJedis(db);
    assertThat(jedis.get(key)).isEqualTo(value);
    jedis.close();
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should be:$")
  public void theRedisKeyInTheDbShouldBeColon(final String key, final Integer db, final String value)
      throws Throwable {
    this.theRedisKeyInTheDbShouldBe(key, db, value);
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should be file \"([^\"]*)\"$")
  public void theRedisKeyInTheDbShouldBeFile(final String key, final Integer db, final String value)
      throws Throwable {
    this.theRedisKeyInTheDbShouldBe(key, db, Utility.fileContent(value));
  }

  @Given("^I have the redis key \"([^\"]*)\"(?: in the db (\\d+))? with value \"([^\"]*)\" with ttl (\\d+) seconds$")
  public void iHaveTheRedisKeyInTheDbWithValueWithTTL(final String key, final Integer db,
                                                      final String value, final int seconds) throws Throwable {
    this.iHaveTheRedisKeyWithValue(key, db, value);
    final Jedis jedis = getJedis(db);
    jedis.expire(key, seconds);
    jedis.close();
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should not exists after (\\d+) seconds$")
  public void theRedisKeyInTheDbShouldNotExistsAfterSeconds(final String key, final Integer db,
                                                            final int seconds) throws Throwable {
    Thread.sleep(seconds * 1_000);
    this.theRedisKeyInTheDbShouldNotExists(key, db);
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should exists$")
  public void theRedisKeyInTheDbShouldExists(final String key, final Integer db) throws Throwable {
    final Jedis jedis = getJedis(db);
    assertThat(jedis.get(key)).isNotNull();
    jedis.close();
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should not exists$")
  public void theRedisKeyInTheDbShouldNotExists(final String key, final Integer db) throws Throwable {
    final Jedis jedis = getJedis(db);
    assertThat(jedis.get(key)).isNull();
    jedis.close();
  }

  @Then("^the redis keys(?: in the db (\\d+))? should exists:$")
  public void theRedisKeysInTheDbShouldExistsColon(final Integer db, final DataTable dataTable)
      throws Throwable {
    for (final String key : dataTable.asList(String.class)) {
      this.theRedisKeysInTheDbShouldExists(key, db);
    }
  }

  @Then("^the redis keys \"([^\"]*)\"(?: in the db (\\d+))? should exists$")
  public void theRedisKeysInTheDbShouldExists(final String keys, final Integer db) throws Throwable {
    final String[] split = keys.split(",");
    for (final String key : split) {
      this.theRedisKeyInTheDbShouldExists(key, db);
    }
  }

  @Then("^the redis keys \"([^\"]*)\"(?: in the db (\\d+))? should not exists$")
  public void theRedisKeysShouldNotExists(final String keys, final Integer db)
      throws Throwable {
    final String[] split = keys.split(",");
    for (final String key : split) {
      this.theRedisKeyInTheDbShouldNotExists(key, db);
    }
  }
}
