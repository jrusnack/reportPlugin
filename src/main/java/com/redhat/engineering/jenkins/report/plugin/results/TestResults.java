/*
 * Copyright (C) 2012 Red Hat, Inc.     
 * 
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions of the 
 * GNU General Public License v.2.
 * 
 * Authors: Jan Rusnacko (jrusnack at redhat dot com)
 */

package com.redhat.engineering.jenkins.report.plugin.results;

import hudson.model.AbstractBuild;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parts of code were reused from TestNG plugin (credits due to its authors)
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public interface TestResults {
    
    
    public  void addUniqueTests(List<TestResult> testList);
    
    public void setOwner(AbstractBuild<?, ?> owner);
    
    public AbstractBuild<?, ?> getOwner();
    
    public String getName();
    
    public BaseResult getParent();
    
    public void setParent(BaseResult parent);

    /**
     * Updates calculated fields
     */
    public  void tally();

    public List<MethodResult> getFailedTests();

    public List<MethodResult> getSkippedTests();

    public List<MethodResult> getPassedTests();
    
    public List<MethodResult> getFailedConfigs();
    
    public List<MethodResult> getSkippedConfigs();
    
    public List<TestResult> getTestList();
    
    public int getTotalTestCount();
	    
    public int getPassedTestCount();
    
    public int getFailedTestCount();
    
    public int getSkippedTestCount();
    
    public int getFailedConfigCount();
    
    public int getSkippedConfigCount();
    
    
    /**
     * For details see DEVELOPMENT.txt
     * 
     * @return true if corresponds to build
     */
    public  boolean isMatrixBuildTestResult();
    
    /**
     * For details see DEVELOPMENT.txt
     * 
     * @return true if corresponds to run
     */
    public boolean isRunTestResult();
    
    
    /**
     * Returns list of run`s results with either one element (for freestyle 
     * project) or multiple elements (for multiconf project) that correspond 
     * to matrix runs 
     * 
     * @return	List with self in case of freestyle
     */
    public List<TestResults> getRunResults();
    
    public List<String> getRuns();
    
    public String toString();

    public Set<String>  getPackageNames();

    public Map<String, PackageResult> getPackageMap();

    
}
