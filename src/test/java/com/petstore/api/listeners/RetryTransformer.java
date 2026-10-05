package com.petstore.api.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import com.petstore.api.utils.RetryAnalyzer;

@SuppressWarnings("rawtypes")
public class RetryTransformer implements IAnnotationTransformer {
	
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
}