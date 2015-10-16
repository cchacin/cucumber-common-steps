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
  public void I_have_the_redis_scored_member_in_the_db_with_score_and_value(final String key,
      final int database, final String score, final String value) {
    final double scoreValue = Double.parseDouble(score);
    jedis.select(database);
    jedis.zadd(key, scoreValue, value);
  }

  @Given("^I have the redis scored members \"([^\"]*)\"(?: in the db (\\d+))? with values:$")
  public void I_have_the_redis_scored_members_in_the_db_with_values(final String key,
      final int database, final DataTable dataTable) {
    final Map<String, Double> table = dataTable.asMap(String.class, Double.class);
    jedis.select(database);
    jedis.zadd(key, table);
  }

  @Then("^I should have the redis scored member \"([^\"]*)\"(?: in the db (\\d+))? with score \"([^\"]*)\" and value \"([^\"]*)\"$")
  public void I_should_have_the_redis_scored_member_in_the_db_with_score_and_value(
      final String key, final int database, final String score, final String value) {
    final double scoredValue = Double.parseDouble(score);
    jedis.select(database);
    assertThat(jedis.zscore(key, value)).isEqualTo(scoredValue);
  }

  @Then("^I should have the redis scored members \"([^\"]*)\"(?: in the db (\\d+))? with values:$")
  public void I_should_have_the_redis_scored_members_in_the_db_with_values(final String key,
      final int database, final DataTable dataTable) {
    final Map<String, Double> table = dataTable.asMap(String.class, Double.class);
    jedis.select(database);
    for (Map.Entry<String, Double> entry : table.entrySet()) {
      assertThat(jedis.zscore(key, entry.getKey())).isEqualTo(entry.getValue());
    }
  }
}
