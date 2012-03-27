/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin.results;

import java.util.List;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public interface TestResults {

    
    public void addUniqueTests(List<TestResult> testList);

    /**
     * Updates calculated fields
     */
    public void tally();

    public List<MethodResult> getFailedTests();

    public List<MethodResult> getSkippedTests();

    public List<MethodResult> getPassedTests();
    
    public List<TestResult> getTestList();
    
    public int getTotalTestCount();
	    
    public int getPassedTestCount();
    
    public int getFailedTestCount();
    
    public int getSkippedTestCount();
    
    
}
