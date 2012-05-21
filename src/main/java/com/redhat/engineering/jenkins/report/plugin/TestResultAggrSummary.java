package com.redhat.engineering.jenkins.report.plugin;

import java.util.ArrayList;
import java.util.List;

public class TestResultAggrSummary extends TestResultAggr {
	private List<TestResultAggr> testResults = new ArrayList<TestResultAggr>();

	public TestResultAggrSummary() {
		super(null, 0, 0, 0);
	}
	
	public TestResultAggrSummary addTestResult(TestResultAggr testResult) {
		testResults.add(testResult);
		
		tests += testResult.getTests();
		success += testResult.getSuccess();
		failed += testResult.getFailed();
		skipped += testResult.getSkipped();
		
		return this;
	}
	
	public List<TestResultAggr> getTestResults() {
		return testResults;
	}
	
}
