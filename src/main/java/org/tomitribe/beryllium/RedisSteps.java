package org.tomitribe.beryllium;

import cucumber.api.java.en.Given;
import redis.clients.jedis.Jedis;

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
