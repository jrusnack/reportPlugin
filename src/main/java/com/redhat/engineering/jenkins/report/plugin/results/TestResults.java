
package com.redhat.engineering.jenkins.report.plugin.results;

import java.util.*;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public abstract class TestResults extends BaseResult implements RunTestResults{
    private List<MethodResult> passedTests = new ArrayList<MethodResult>();
    private List<MethodResult> failedTests = new ArrayList<MethodResult>();
    private List<MethodResult> skippedTests = new ArrayList<MethodResult>();
    private int passedTestCount;
    private int failedTestCount;
    private int skippedTestCount;
    private int totalTestCount;
    
   private Map<String, PackageResult> packageMap = new HashMap<String, PackageResult>();
    
    // stores list of all tests performed 
    private List<TestResult> testList = new ArrayList<TestResult>();
    
    // stores list of all runs: only one for freestyle project, multiple for matrix
    private List<TestResults> runs = new ArrayList<TestResults>();
    private List<RunTestResults> runTestResults = new ArrayList<RunTestResults>();

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
    
    public abstract boolean isMatrixBuildTestResult();
    
    
    /**
     * Returns list of runs with either one element (for freestyle project) or 
     * multiple elements (for multiconf project) that correspond to matrix runs 
     * 
     * @return	List with self in case of freestyle
     */
    public List<RunTestResults> getRuns(){
	// if freestyle, add self as the only run 
	if( runTestResults == null ){
	    runTestResults = new ArrayList<RunTestResults>();
	    runTestResults.add(this);
	    return runTestResults;
	} else {
	    return runTestResults;
	}
    }
    
    public String toString(){
	return String.format("TestResults {name='%s', totalTests=%d, " +
          "failedTests=%d, skippedTests=%d}", name, totalTestCount, failedTestCount,
          skippedTestCount);
    }

    public Set<String>  getPackageNames() {
	return packageMap.keySet();
    }

    public Map<String, PackageResult> getPackageMap() {
	return packageMap;
    }
}
