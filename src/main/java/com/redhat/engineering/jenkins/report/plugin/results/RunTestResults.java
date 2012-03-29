/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin.results;

import java.util.List;

/**
 * 
 * This is analogue of abstract class hudson.tasks.test.TestResult
 * 
 * This interface will be implemented by two classes: one for freestyle projects 
 * and one for matrix(multiconfiguration) projects. 
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public interface RunTestResults {
    
    public int getPassedTestCount();
    
    public int getFailedTestCount();
    
    public int getSkippedTestCount();
    
    public int getSkippedConfigCount();
    
    public int getFailedConfigCount();
    
    public List<MethodResult> getFailedTests();

    public List<MethodResult> getPassedTests();

    public List<MethodResult> getSkippedTests();
    
    /**
     * Method is marked with attribute is-config=true in TestNG XML if it doesn`t
     * do testing, just configures environment, like @BeforeTest, @SetUp, @TearDown 
     * etc. (so no real test)
     * 
     * @return	list of failed configuration methods
     */
    public List<MethodResult> getFailedConfigs();

    /**
     * Method is marked with attribute is-config=true in TestNG XML if it doesn`t
     * do testing, just configures environment, like @BeforeTest, @SetUp, @TearDown 
     * etc. (so no real test)
     * 
     * @return list of skipped configuration methods
     */
    public List<MethodResult> getSkippedConfigs();

    public void addUniqueTests(List<TestResult> testList);

    
    /**
     * Update all fields that are calculated from others
     */
    public void tally();

    
}
