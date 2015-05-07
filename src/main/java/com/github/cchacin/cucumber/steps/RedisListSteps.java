package com.github.cchacin.cucumber.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import redis.clients.jedis.Jedis;

import java.util.List;

import static com.github.cchacin.cucumber.steps.Utility.fileContent;
import static org.assertj.core.api.Assertions.assertThat;

public class RedisListSteps {

    private final Jedis jedis = new Jedis("localhost");

    @Given("^I have the redis list \"([^\"]*)\" with values \"([^\"]*)\"$")
    public void I_have_the_redis_list_with_values(final String list, final String values) throws Throwable {
        jedis.lpush(list, values.split(","));
    }

    @Given("^I have the redis list \"([^\"]*)\" with values in file \"([^\"]*)\"$")
    public void I_have_the_redis_list_with_values_in_file(final String list, String filename) throws Throwable {
        jedis.lpush(list, fileContent(filename).split("\n"));
    }

    @Given("^I have the redis list \"([^\"]*)\" with values:$")
    public void I_have_the_redis_list_with_values(final String list, final DataTable dataTable) throws Throwable {
        final List<String> table = dataTable.asList(String.class);
        jedis.lpush(list, table.toArray(new String[table.size()]));
    }

    @Given("^I have the redis list \"([^\"]*)\" with values \"([^\"]*)\" with ttl (\\d+) seconds$")
    public void I_have_the_redis_list_with_values_with_ttl_seconds(final String list, final String values, final int seconds) throws Throwable {
        jedis.lpush(list, values.split(","));
    }

    @Then("^the redis list \"([^\"]*)\" should be \"([^\"]*)\"$")
    public void the_redis_list_should_be(String list, String values) throws Throwable {
        for (final String value : values.split(",")) {
            assertThat(values).contains(jedis.lpop(list));
        }
    }

    @Then("^the redis list \"([^\"]*)\" should be:$")
    public void the_redis_list_should_be(final String list, final DataTable dataTable) throws Throwable {
        final List<String> table = dataTable.asList(String.class);
        for (final String value : table) {
            assertThat(table).contains(jedis.lpop(list));
        }
    }

    @Then("^the redis list \"([^\"]*)\" should exists$")
    public void the_redis_list_should_exists(final String list) throws Throwable {
        assertThat(jedis.exists(list)).isTrue();
    }

    @Then("^the redis lists \"([^\"]*)\" should exists$")
    public void the_redis_lists_should_exists(final String list) throws Throwable {
        for (final String key : list.split(",")) {
            assertThat(jedis.exists(key)).isTrue();
        }
    }

    @Then("^the redis lists should exists:$")
    public void the_redis_lists_should_exists(final DataTable dataTable) throws Throwable {
        for (final String key : dataTable.asList(String.class)) {
            assertThat(jedis.exists(key)).isTrue();
        }
    }

    @Then("^the redis list \"([^\"]*)\" should not exists after (\\d+) seconds$")
    public void the_redis_list_should_not_exists_after_seconds(final String list, final int seconds) throws Throwable {
        Thread.sleep(seconds * 1_000);
        this.the_redis_lists_should_not_exists(list);
    }

    @Then("^the redis list \"([^\"]*)\" should not exists$")
    public void the_redis_list_should_not_exists(final String list) throws Throwable {
        this.the_redis_list_should_not_exists_after_seconds(list, 1);
    }

    @Then("^the redis lists \"([^\"]*)\" should not exists$")
    public void the_redis_lists_should_not_exists(final String list) throws Throwable {
        for (final String key : list.split(",")) {
            assertThat(jedis.exists(key)).isFalse();
        }
    }

    @Then("^the redis list \"([^\"]*)\" should be file \"([^\"]*)\"$")
    public void the_redis_list_should_be_file(final String list, final String filename) throws Throwable {
        final String[] values = fileContent(filename).split("\n");
        for (final String value : values) {
            assertThat(values).contains(jedis.lpop(list));
        }
    }
}
