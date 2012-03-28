
package com.redhat.engineering.jenkins.report.plugin.results;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public abstract class TestResults extends BaseResult{
    private List<MethodResult> passedTests = new ArrayList<MethodResult>();
    private List<MethodResult> failedTests = new ArrayList<MethodResult>();
    private List<MethodResult> skippedTests = new ArrayList<MethodResult>();
    private int passedTestCount;
    private int failedTestCount;
    private int skippedTestCount;
    private int totalTestCount;
    private List<TestResult> testList = new ArrayList<TestResult>();

    public TestResults(String name){
	super(name);
    }
    
    public void addUniqueTests(List<TestResult> testList);

    /**
     * Updates calculated fields
     */
    public void tally();

    public List<MethodResult> getFailedTests(){
	return failedTests;
    }

    public List<MethodResult> getSkippedTests(){
	return skippedTests;
    }

    public List<MethodResult> getPassedTests(){
	return passedTests;
    }
    
    public List<TestResult> getTestList(){
	return testList;
    }
    
    public int getTotalTestCount(){
	return totalTestCount;
    }
	    
    public int getPassedTestCount(){
	return passedTestCount;
    }
    
    public int getFailedTestCount(){
	return failedTestCount;
    }
    
    public int getSkippedTestCount(){
	return skippedTestCount;
    }
    
    public abstract boolean isMatrixTestResult();
    
    public List<MatrixRunTestResults> getRuns();
}
