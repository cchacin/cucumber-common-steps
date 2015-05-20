/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.cchacin.cucumber.steps;

import static com.github.cchacin.cucumber.steps.Utility.fileContent;
import static org.assertj.core.api.Assertions.assertThat;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


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
  public void I_have_the_redis_key_with_value(final String key, final Integer db, final String value)
      throws Throwable {
    final Jedis jedis = getJedis(db);
    jedis.set(key, value);
    jedis.close();
  }

  @Given("^I have the redis key \"([^\"]*)\"(?: in the db (\\d+))? with value in file \"([^\"]*)\"$")
  public void I_have_the_redis_key_with_value_in_file(final String key, final Integer db,
      final String filename) throws Throwable {
    this.I_have_the_redis_key_with_value(key, db, fileContent(filename));
  }

  @Given("^I have the redis key \"([^\"]*)\"(?: in the db (\\d+))? with value:$")
  public void I_have_the_redis_key_with_value_(final String key, final Integer db,
      final String value) throws Throwable {
    this.I_have_the_redis_key_with_value(key, db, value);
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should be \"([^\"]*)\"$")
  public void the_redis_key_should_be(final String key, final Integer db, final String value)
      throws Throwable {
    final Jedis jedis = getJedis(db);
    assertThat(jedis.get(key)).isEqualTo(value);
    jedis.close();
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should be:$")
  public void the_redis_key_should_be_(final String key, final Integer db, final String value)
      throws Throwable {
    this.the_redis_key_should_be(key, db, value);
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should be file \"([^\"]*)\"$")
  public void the_redis_key_should_be_file(final String key, final Integer db, final String value)
      throws Throwable {
    this.the_redis_key_should_be(key, db, fileContent(value));
  }

  @Given("^I have the redis key \"([^\"]*)\"(?: in the db (\\d+))? with value \"([^\"]*)\" with ttl (\\d+) seconds$")
  public void I_have_the_redis_key_with_value_with_ttl(final String key, final Integer db,
      final String value, final int seconds) throws Throwable {
    this.I_have_the_redis_key_with_value(key, db, value);
    final Jedis jedis = getJedis(db);
    jedis.expire(key, seconds);
    jedis.close();
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should not exists after (\\d+) seconds$")
  public void the_redis_key_should_not_exists_after_seconds(final String key, final Integer db,
      final int seconds) throws Throwable {
    Thread.sleep(seconds * 1_000);
    this.the_redis_key_should_not_exists(key, db);
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should exists$")
  public void the_redis_key_should_exists(final String key, final Integer db) throws Throwable {
    final Jedis jedis = getJedis(db);
    assertThat(jedis.get(key)).isNotNull();
    jedis.close();
  }

  @Then("^the redis key \"([^\"]*)\"(?: in the db (\\d+))? should not exists$")
  public void the_redis_key_should_not_exists(final String key, final Integer db) throws Throwable {
    final Jedis jedis = getJedis(db);
    assertThat(jedis.get(key)).isNull();
    jedis.close();
  }

  @Then("^the redis keys(?: in the db (\\d+))? should exists:$")
  public void the_redis_keys_should_exists(final Integer db, final DataTable dataTable)
      throws Throwable {
    for (final String key : dataTable.asList(String.class)) {
      this.the_redis_keys_should_exists(key, db);
    }
  }

  @Then("^the redis keys \"([^\"]*)\"(?: in the db (\\d+))? should exists$")
  public void the_redis_keys_should_exists(final String keys, final Integer db) throws Throwable {
    final String[] split = keys.split(",");
    for (final String key : split) {
      this.the_redis_key_should_exists(key, db);
    }
  }

  @Then("^the redis keys \"([^\"]*)\"(?: in the db (\\d+))? should not exists$")
  public void the_redis_keys_should_not_exists(final String keys, final Integer db)
      throws Throwable {
    final String[] split = keys.split(",");
    for (final String key : split) {
      this.the_redis_key_should_not_exists(key, db);
    }
  }
}
