package com.petstore.api.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	private int attempts = 0;

	private static final int MAX_ATTEMPTS = 2;

	@Override
	public boolean retry(ITestResult result) {

		if (attempts < MAX_ATTEMPTS) {

			attempts++;

			return true;
		}

		return false;
	}
}