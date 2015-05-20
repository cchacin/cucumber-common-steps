package com.github.cchacin.cucumber.steps;

import redis.clients.jedis.Jedis;
import cucumber.api.java.en.Given;

public class RedisSteps {
  private final Jedis jedis = new Jedis("localhost");

  @Given("^I have cleaned redis(?: db (\\d+))?$")
  public void I_have_cleaned_redis_db(final Integer db) {
    if (db == null) {
      jedis.flushAll();
    } else {
      jedis.select(db);
      jedis.flushDB();
    }
  }
}
