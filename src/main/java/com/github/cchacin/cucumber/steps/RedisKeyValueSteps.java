package com.github.cchacin.cucumber.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import redis.clients.jedis.Jedis;

import static com.github.cchacin.cucumber.steps.Utility.fileContent;
import static org.assertj.core.api.Assertions.assertThat;


public class RedisKeyValueSteps {
    private final Jedis jedis = new Jedis("localhost");

    @Given("^I have the redis key \"([^\"]*)\" in database (\\d+) with value \"([^\"]*)\"$")
    public void I_have_the_redis_key_in_database_with_value(final String key, final int database, final String value) throws Throwable {
        jedis.select(database);
        jedis.set(key, value);
    }

    @Given("^I have the redis key \"([^\"]*)\" in database (\\d+) with value in file \"([^\"]*)\"$")
    public void I_have_the_redis_key_in_database_with_value_in_file(final String key, final int database, final String filename) throws Throwable {
        this.I_have_the_redis_key_in_database_with_value(key, database, fileContent(filename));
    }

    @Given("^I have the redis key \"([^\"]*)\" in database (\\d+) with value:$")
    public void I_have_the_redis_key_in_database_with_value_(final String key, final int database, final String value) throws Throwable {
        this.I_have_the_redis_key_in_database_with_value(key, database, value);
    }

    @Then("^the redis key \"([^\"]*)\" in database (\\d+) should be \"([^\"]*)\"$")
    public void the_redis_key_in_database_should_be(final String key, final int database, final String value) throws Throwable {
        jedis.select(database);
        assertThat(jedis.get(key)).isEqualTo(value);
    }

    @Then("^the redis key \"([^\"]*)\" in database (\\d+) should be:$")
    public void the_redis_key_in_database_should_be_(final String key, final int database, final String value) throws Throwable {
        this.the_redis_key_in_database_should_be(key, database, value);
    }

    @Then("^the redis key \"([^\"]*)\" in database (\\d+) should be file \"([^\"]*)\"$")
    public void the_redis_key_in_database_should_be_file(final String key, final int database, final String value) throws Throwable {
        this.the_redis_key_in_database_should_be(key, database, fileContent(value));
    }

    @Given("^I have the redis key \"([^\"]*)\" in database (\\d+) with value \"([^\"]*)\" with ttl (\\d+) seconds$")
    public void I_have_the_redis_key_in_database_with_value_with_ttl(final String key, final int database, final String value, final int seconds) throws Throwable {
        this.I_have_the_redis_key_in_database_with_value(key, database, value);
        jedis.select(database);
        jedis.expire(key, seconds);
    }

    @Then("^the redis key \"([^\"]*)\" in database (\\d+) should not exist after (\\d+) seconds$")
    public void the_redis_key_in_database_should_not_exist_after_seconds(final String key, final int database, final int seconds) throws Throwable {
        Thread.sleep(seconds * 1_000);
        this.the_redis_key_should_not_exist_in_database(key, database);
    }

    @Then("^the redis key \"([^\"]*)\" should exist in database (\\d+)$")
    public void the_redis_key_should_exist_in_database(final String key, final int database) throws Throwable {
        jedis.select(database);
        assertThat(jedis.get(key)).isNotNull();
    }

    @Then("^the redis key \"([^\"]*)\" should not exist in database (\\d+)$")
    public void the_redis_key_should_not_exist_in_database(final String key, final int database) throws Throwable {
        jedis.select(database);
        assertThat(jedis.get(key)).isNull();
    }

    @Then("^the redis keys should exist in database (\\d+):$")
    public void the_redis_keys_should_exist_in_database(final int database, final DataTable dataTable) throws Throwable {
        for (final String key : dataTable.asList(String.class)) {
            this.the_redis_keys_should_exist_in_database(key, database);
        }
    }

    @Then("^the redis keys \"([^\"]*)\" should exist in database (\\d+)$")
    public void the_redis_keys_should_exist_in_database(final String keys, final int database) throws Throwable {
        final String[] split = keys.split(",");
        for (final String key : split) {
            this.the_redis_key_should_exist_in_database(key, database);
        }
    }

    @Then("^the redis keys \"([^\"]*)\" should not exist in database (\\d+)$")
    public void the_redis_keys_should_not_exist_in_database(final String keys, final int database) throws Throwable {
        final String[] split = keys.split(",");
        for (final String key : split) {
            this.the_redis_key_should_not_exist_in_database(key, database);
        }
    }
}
