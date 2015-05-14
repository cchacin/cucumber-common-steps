package com.github.cchacin.cucumber.steps;

import cucumber.api.java.en.Given;
import redis.clients.jedis.Jedis;

public class RedisSteps {
    private final Jedis jedis = new Jedis("localhost");

    @Given("^I have cleaned db (\\d+)$")
    public void I_have_cleaned_db(final int db) {
        jedis.select(db);
        jedis.flushDB();
    }
}
