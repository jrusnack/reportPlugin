package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.results.Filter;
import com.redhat.engineering.jenkins.report.plugin.results.MatrixBuildTestResults;

public class TestResultAggrSummary{
	private int tests;
	private int passed;
	private int failed;
	private int skipped;
        
	public TestResultAggrSummary() {
	}
	
	public TestResultAggrSummary addTestResult(MatrixBuildTestResults testResult, Filter filter) {
            // FIXME: problem with parallel  access - do this atomically (multiple user add filter to the same results)
            if(filter.getCombinationFilter() != null) testResult.addFilter(filter);

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
