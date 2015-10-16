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
package org.tomitribe.beryllium;

import java.util.List;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static com.google.common.truth.Truth.assertThat;
import static org.tomitribe.beryllium.Utility.fileContent;

public class RedisListSteps {

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

  @Given("^I have the redis list \"([^\"]*)\"(?: in the db (\\d+))? with values \"([^\"]*)\"$")
  public void I_have_the_redis_list_with_values(final String list, final Integer db,
      final String values) throws Throwable {
    final Jedis jedis = getJedis(db);
    jedis.lpush(list, values.split(","));
    jedis.close();
  }

  @Given("^I have the redis list \"([^\"]*)\"(?: in the db (\\d+))? with values in file \"([^\"]*)\"$")
  public void I_have_the_redis_list_with_values_in_file(final String list, final Integer db,
      String filename) throws Throwable {
    this.I_have_the_redis_list_with_values(list, db, fileContent(filename.trim()));
  }

  @Given("^I have the redis list \"([^\"]*)\"(?: in the db (\\d+))? with values:$")
  public void I_have_the_redis_list_with_values(final String list, final Integer db,
      final DataTable dataTable) throws Throwable {
    final Jedis jedis = getJedis(db);
    final List<String> table = dataTable.asList(String.class);
    jedis.lpush(list, table.toArray(new String[table.size()]));
    jedis.close();
  }

  @Given("^I have the redis list \"([^\"]*)\"(?: in the db (\\d+))? with values \"([^\"]*)\" with ttl (\\d+) seconds$")
  public void I_have_the_redis_list_with_values_with_ttl_seconds(final String list,
      final Integer db, final String values, final int seconds) throws Throwable {
    final Jedis jedis = getJedis(db);
    jedis.lpush(list, values.split(","));
    jedis.expire(list, seconds);
    jedis.close();
  }

  @Then("^the redis list \"([^\"]*)\"(?: in the db (\\d+))? should be \"([^\"]*)\"$")
  public void the_redis_list_should_be(String list, final Integer db, String values)
      throws Throwable {
    final Jedis jedis = getJedis(db);
    for (final String value : values.split(",")) {
      assertThat(values).contains(jedis.lpop(list));
    }
    jedis.close();
  }

  @Then("^the redis list \"([^\"]*)\"(?: in the db (\\d+))? should be:$")
  public void the_redis_list_should_be(final String list, final Integer db,
      final DataTable dataTable) throws Throwable {
    final Jedis jedis = getJedis(db);
    final List<String> table = dataTable.asList(String.class);
    for (final String value : table) {
      assertThat(table).contains(jedis.lpop(list));
    }
    jedis.close();
  }

  @Then("^the redis list \"([^\"]*)\"(?: in the db (\\d+))? should exists$")
  public void the_redis_list_should_exists(final String list, final Integer db) throws Throwable {
    final Jedis jedis = getJedis(db);
    assertThat(jedis.exists(list)).isTrue();
    jedis.close();
  }

  @Then("^the redis lists \"([^\"]*)\"(?: in the db (\\d+))? should exists$")
  public void the_redis_lists_should_exists(final String list, final Integer db) throws Throwable {
    final Jedis jedis = getJedis(db);
    for (final String key : list.split(",")) {
      assertThat(jedis.exists(key)).isTrue();
    }
    jedis.close();
  }

  @Then("^the redis lists(?: in the db (\\d+))? should exists:$")
  public void the_redis_lists_should_exists(final Integer db, final DataTable dataTable)
      throws Throwable {
    final Jedis jedis = getJedis(db);
    for (final String key : dataTable.asList(String.class)) {
      assertThat(jedis.exists(key)).isTrue();
    }
    jedis.close();
  }

  @Then("^the redis list \"([^\"]*)\"(?: in the db (\\d+))? should not exists after (\\d+) seconds$")
  public void the_redis_list_should_not_exists_after_seconds(final String list, final Integer db,
      final int seconds) throws Throwable {
    Thread.sleep(seconds * 1_000);
    this.the_redis_lists_should_not_exists(list, db);
  }

  @Then("^the redis list \"([^\"]*)\"(?: in the db (\\d+))? should not exists$")
  public void the_redis_list_should_not_exists(final String list, final Integer db)
      throws Throwable {
    this.the_redis_list_should_not_exists_after_seconds(list, db, 1);
  }

  @Then("^the redis lists \"([^\"]*)\"(?: in the db (\\d+))? should not exists$")
  public void the_redis_lists_should_not_exists(final String list, final Integer db)
      throws Throwable {
    final Jedis jedis = getJedis(db);
    for (final String key : list.split(",")) {
      assertThat(jedis.exists(key)).isFalse();
    }
    jedis.close();
  }

  @Then("^the redis list \"([^\"]*)\"(?: in the db (\\d+))? should be file \"([^\"]*)\"$")
  public void the_redis_list_should_be_file(final String list, final Integer db,
      final String filename) throws Throwable {
    final Jedis jedis = getJedis(db);
    final String[] values = fileContent(filename).split("\n");
    assertThat(values).asList().contains(jedis.lpop(list));
    jedis.close();
  }
}
