/*
 * Copyright (C) 2012 Red Hat, Inc.     
 * 
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions of the 
 * GNU General Public License v.2.
 * 
 * Authors: Jan Rusnacko (jrusnack at redhat dot com)
 */

package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.results.Filter;
import com.redhat.engineering.jenkins.report.plugin.results.MatrixBuildTestResults;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jrusnack
 */
public class ReportPluginTestAggregator {
    private Map<Integer, MatrixBuildTestResults> testResults;
    
    public ReportPluginTestAggregator(){
        testResults = new HashMap<Integer, MatrixBuildTestResults>();
    }
    
    public void addBuildResults(Integer build, MatrixBuildTestResults results){
        testResults.put(build, results);
    }
    
    public MatrixBuildTestResults getBuildResults(Integer build){
        return testResults.get(build);
    }
    
    public int getPassedTestCount(Integer build) {
        return testResults.get(build).getPassedTestCount();
    }
    
    public int getFailedTestCount(Integer build) {
        return testResults.get(build).getFailedTestCount();
    }
    
    public int getSkippedTestCount(Integer build) {
        return testResults.get(build).getSkippedTestCount();
    }
    
    public void addFilter(Integer build, Filter filter){
        testResults.get(build).addFilter(filter);
    }
    
    public void removeFilter(Integer build){
        testResults.get(build).removeFilter();
    }
    
    public boolean containsKey(Integer build){
        return testResults.containsKey(build);
    }
    
}
