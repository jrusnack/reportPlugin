package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.results.MatrixBuildTestResults;

public class TestResultAggrSummary{
	private int tests;
	private int passed;
	private int failed;
	private int skipped;
        
	public TestResultAggrSummary() {
	}
	
	public TestResultAggrSummary addTestResult(MatrixBuildTestResults testResult) {
		
		tests += testResult.getTotalTestCount();
		passed += testResult.getPassedTestCount();
		failed += testResult.getFailedTestCount();
		skipped += testResult.getSkippedTestCount();
		
		return this;
	}

    public Integer getFailed() {
        return failed;
    }

    public Integer getSkipped() {
        return skipped;
    }

    public Integer getPassed() {
        return passed;
    }
        
        
	
}
