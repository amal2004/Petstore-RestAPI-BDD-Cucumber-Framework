package com.petstore.api.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import org.testng.annotations.Listeners;
import com.petstore.api.listeners.RetryTransformer;

@CucumberOptions(features = "src/test/resources/features/pet/upload_pet_image.feature", 
				 glue = "com.petstore.api.stepdefinitions", 
				 tags = "@only", plugin = {
		               "pretty", "html:target/cucumber-reports.html"
})
@Listeners(RetryTransformer.class)
public class TestRunner extends AbstractTestNGCucumberTests {
}
