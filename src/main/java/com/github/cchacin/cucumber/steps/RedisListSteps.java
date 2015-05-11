package com.github.cchacin.cucumber.steps;

import java.util.List;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import redis.clients.jedis.Jedis;

import static com.github.cchacin.cucumber.steps.Utility.fileContent;
import static org.assertj.core.api.Assertions.assertThat;

public class RedisListSteps {

    private final Jedis jedis = new Jedis("localhost");

    @Given("^I have the redis list \"([^\"]*)\" in database (\\d+) with values \"([^\"]*)\"$")
    public void I_have_the_redis_list_in_database_with_values(final String list, final int database, final String values) throws Throwable {
        jedis.select(database);
        jedis.lpush(list, values.split(","));
    }

    @Given("^I have the redis list \"([^\"]*)\" in database (\\d+) with values in file \"([^\"]*)\"$")
    public void I_have_the_redis_list_in_database_with_values_in_file(final String list, final int database, String filename) throws Throwable {
        jedis.select(database);
        jedis.lpush(list, fileContent(filename).split("\n"));
    }

    @Given("^I have the redis list \"([^\"]*)\" in database (\\d+) with values:$")
    public void I_have_the_redis_list_in_database_with_values(final String list, final int database, final DataTable dataTable) throws Throwable {
        final List<String> table = dataTable.asList(String.class);
        jedis.select(database);
        jedis.lpush(list, table.toArray(new String[table.size()]));
    }

    @Given("^I have the redis list \"([^\"]*)\" in database (\\d+) with values \"([^\"]*)\" with ttl (\\d+) seconds$")
    public void I_have_the_redis_list_in_database_with_values_with_ttl_seconds(final String list, final int database, final String values, final int seconds) throws Throwable {
        jedis.select(database);
        jedis.lpush(list, values.split(","));
    }

    @Then("^the redis list \"([^\"]*)\" in database (\\d+) should be \"([^\"]*)\"$")
    public void the_redis_list_in_database_should_be(final String list, final int database, final String values) throws Throwable {
        jedis.select(database);
        for (final String value : values.split(",")) {
            assertThat(values).contains(jedis.lpop(list));
        }
    }

    @Then("^the redis list \"([^\"]*)\" in database (\\d+) should be:$")
    public void the_redis_list_in_database_should_be(final String list, final int database, final DataTable dataTable) throws Throwable {
        jedis.select(database);
        final List<String> table = dataTable.asList(String.class);
        for (final String value : table) {
            assertThat(table).contains(jedis.lpop(list));
        }
    }

    @Then("^the redis list \"([^\"]*)\" should exist in database (\\d+)$")
    public void the_redis_list_should_exist_in_database(final String list, final int database) throws Throwable {
        jedis.select(database);
        assertThat(jedis.exists(list)).isTrue();
    }

    @Then("^the redis lists \"([^\"]*)\" should exist in database (\\d+)$")
    public void the_redis_lists_should_exist_in_database(final String list, final int database) throws Throwable {
        jedis.select(database);
        for (final String key : list.split(",")) {
            assertThat(jedis.exists(key)).isTrue();
        }
    }

    @Then("^the redis lists should exist in database (\\d+):$")
    public void the_redis_lists_should_exist_in_database(final int database, final DataTable dataTable) throws Throwable {
        jedis.select(database);
        for (final String key : dataTable.asList(String.class)) {
            assertThat(jedis.exists(key)).isTrue();
        }
    }

    @Then("^the redis list \"([^\"]*)\" in database (\\d+) should not exist after (\\d+) seconds$")
    public void the_redis_list_in_database_should_not_exist_after_seconds(final String list, final int database, final int seconds) throws Throwable {
        Thread.sleep(seconds * 1_000);
        this.the_redis_lists_should_not_exist_in_database(list, database);
    }

    @Then("^the redis list \"([^\"]*)\" should not exist in database (\\d+)$")
    public void the_redis_list_should_not_exist_in_database(final String list, final int database) throws Throwable {
        this.the_redis_list_in_database_should_not_exist_after_seconds(list, database, 1);
    }

    @Then("^the redis lists \"([^\"]*)\" should not exist in database (\\d+)$")
    public void the_redis_lists_should_not_exist_in_database(final String list, final int database) throws Throwable {
        jedis.select(database);
        for (final String key : list.split(",")) {
            assertThat(jedis.exists(key)).isFalse();
        }
    }

    @Then("^the redis list \"([^\"]*)\" in database (\\d+) should be file \"([^\"]*)\"$")
    public void the_redis_list_in_database_should_be_file(final String list, final int database, final String filename) throws Throwable {
        jedis.select(database);
        final String[] values = fileContent(filename).split("\n");
        for (final String value : values) {
            assertThat(values).contains(jedis.lpop(list));
        }
    }
}
