package com.petstore.api.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import com.petstore.api.api.PetApi;
import com.petstore.api.api.StoreApi;
import com.petstore.api.api.UserApi;
import com.petstore.api.utils.ScenarioContext;

public class Hooks {

	private static final ThreadLocal<ScenarioContext> CTX_HOLDER = ThreadLocal.withInitial(ScenarioContext::new);

	public static ScenarioContext ctx() {
		return CTX_HOLDER.get();
	}

	private final PetApi petApi = new PetApi();
	private final StoreApi storeApi = new StoreApi();
	private final UserApi userApi = new UserApi();

	@Before
	public void beforeScenario(Scenario scenario) {

		ctx().clear();

		String testCaseId = extractTestCaseId(scenario.getName());

	}

	@After
	public void afterScenario(Scenario scenario) {

		String testCaseId = extractTestCaseId(scenario.getName());

		cleanupTestData(testCaseId);

		CTX_HOLDER.remove();
	}

	private void cleanupTestData(String testCaseId) {
		ScenarioContext c = ctx();

		Long petId = c.get("petId", Long.class);
		if (petId != null) {
			try {
				petApi.delete(petId, null);
			} catch (Exception e) {
				System.out.println(
						"[cleanup] " + testCaseId + ": failed to delete pet " + petId + " - " + e.getMessage());
			}
		}

		Long orderId = c.get("orderId", Long.class);
		if (orderId != null) {
			try {
				storeApi.deleteOrder(orderId);
			} catch (Exception e) {
				System.out.println(
						"[cleanup] " + testCaseId + ": failed to delete order " + orderId + " - " + e.getMessage());
			}
		}

		String username = c.get("username", String.class);
		if (username != null) {
			try {
				userApi.delete(username);
			} catch (Exception e) {
				System.out.println(
						"[cleanup] " + testCaseId + ": failed to delete user " + username + " - " + e.getMessage());
			}
		}
	}

	private String extractTestCaseId(String scenarioName) {

		if (scenarioName == null || scenarioName.isBlank()) {
			return "N/A";
		}

		int separatorIndex = scenarioName.indexOf(' ');

		if (separatorIndex > 0) {
			return scenarioName.substring(0, separatorIndex).trim();
		}

		return "N/A";
	}
}
