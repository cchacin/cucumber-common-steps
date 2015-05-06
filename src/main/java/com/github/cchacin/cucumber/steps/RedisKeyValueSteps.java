package com.github.cchacin.cucumber.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import redis.clients.jedis.Jedis;

import static com.github.cchacin.cucumber.steps.Utility.fileContent;
import static org.assertj.core.api.Assertions.assertThat;


public class RedisKeyValueSteps {
    private final Jedis jedis = new Jedis("localhost");

    @Given("^I have the redis key \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void I_have_the_redis_key_with_value(final String key, final String value) throws Throwable {
        jedis.set(key, value);
    }

    @Given("^I have the redis key \"([^\"]*)\" with value in file \"([^\"]*)\"$")
    public void I_have_the_redis_key_with_value_in_file(final String key, final String filename) throws Throwable {
        this.I_have_the_redis_key_with_value(key, fileContent(filename));
    }

    @Given("^I have the redis key \"([^\"]*)\" with value:$")
    public void I_have_the_redis_key_with_value_(final String key, final String value) throws Throwable {
        this.I_have_the_redis_key_with_value(key, value);
    }

    @Then("^the redis key \"([^\"]*)\" should be \"([^\"]*)\"$")
    public void the_redis_key_should_be(final String key, final String value) throws Throwable {
        assertThat(jedis.get(key)).isEqualTo(value);
    }

    @Then("^the redis key \"([^\"]*)\" should be:$")
    public void the_redis_key_should_be_(final String key,  final String value) throws Throwable {
        this.the_redis_key_should_be(key, value);
    }

    @Then("^the redis key \"([^\"]*)\" should be file \"([^\"]*)\"$")
    public void the_redis_key_should_be_file(final String key, final String value) throws Throwable {
        this.the_redis_key_should_be(key, fileContent(value));
    }

    @Given("^I have the redis key \"([^\"]*)\" with value \"([^\"]*)\" with ttl (\\d+) seconds$")
    public void I_have_the_redis_key_with_value_with_ttl(final String key, final String value, final int seconds) throws Throwable {
        this.I_have_the_redis_key_with_value(key, value);
        jedis.expire(key, seconds);
    }

    @Then("^the redis key \"([^\"]*)\" should not exists after (\\d+) seconds$")
    public void the_redis_key_should_not_exists_after_seconds(final String key, final int seconds) throws Throwable {
        Thread.sleep(seconds * 1_000);
        this.the_redis_key_should_not_exists(key);
    }

    @Then("^the redis key \"([^\"]*)\" should exists$")
    public void the_redis_key_should_exists(final String key) throws Throwable {
        assertThat(jedis.get(key)).isNotNull();
    }

    @Then("^the redis key \"([^\"]*)\" should not exists$")
    public void the_redis_key_should_not_exists(final String key) throws Throwable {
        assertThat(jedis.get(key)).isNull();
    }

    @Then("^the redis keys should exists:$")
    public void the_redis_keys_should_exists(final DataTable dataTable) throws Throwable {
        for (final String key : dataTable.asList(String.class)) {
            this.the_redis_keys_should_exists(key);
        }
    }

    @Then("^the redis keys \"([^\"]*)\" should exists$")
    public void the_redis_keys_should_exists(final String keys) throws Throwable {
        final String[] split = keys.split(",");
        for (final String key : split) {
            this.the_redis_key_should_exists(key);
        }
    }

    @Then("^the redis keys \"([^\"]*)\" should not exists$")
    public void the_redis_keys_should_not_exists(final String keys) throws Throwable {
        final String[] split = keys.split(",");
        for (final String key : split) {
            this.the_redis_key_should_not_exists(key);
        }
    }
}
