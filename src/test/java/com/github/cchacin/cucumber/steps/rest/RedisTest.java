package com.github.cchacin.cucumber.steps.rest;

import com.github.cchacin.cucumber.steps.RedisKeyValueSteps;
import com.github.cchacin.cucumber.steps.RedisListSteps;
import cucumber.runtime.arquillian.api.Features;
import cucumber.runtime.arquillian.api.Glues;

/**
 * dguerreromartin 5/11/15
 */
@Glues({RedisKeyValueSteps.class, RedisListSteps.class})
@Features({"features/redis.feature"})
public class RedisTest {

}
