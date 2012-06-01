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
import hudson.model.AbstractBuild;
import hudson.model.Run;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author jrusnack
 */
public class ReportPluginTestAggregator {
    private TreeMap<Run, MatrixBuildTestResults> testResults;
    
    public ReportPluginTestAggregator(){
        Comparator<AbstractBuild> comparator = new Comparator<AbstractBuild>(){
            @Override public int compare(AbstractBuild b1, AbstractBuild b2) {
                return b1.compareTo(b2);
            }
        };
        testResults = new TreeMap<Run, MatrixBuildTestResults>();
    }
    
    public void addBuildResults(AbstractBuild build, MatrixBuildTestResults results){
        testResults.put(build, results);
    }
    
    public Set<Run> keySet(){
        return testResults.keySet();
    }
    
    public Run firstKey(){
        return testResults.firstKey();
    }
    
    public Run lastKey(){
        return testResults.lastKey();
    }
    
    public MatrixBuildTestResults getBuildResults(Run Run){
        return testResults.get(Run);
    }
    
    // FIXME: check null
    public int getPassedTestCount(Run run) {
        return testResults.get(run).getPassedTestCount();
    }
    
    public int getFailedTestCount(Run run) {
        return testResults.get(run).getFailedTestCount();
    }
    
    public int getSkippedTestCount(Run run) {
        return testResults.get(run).getSkippedTestCount();
    }
    
    public void addFilter(Run run, Filter filter){
        testResults.get(run).addFilter(filter);
    }
    
    public void removeFilter(Run run){
        testResults.get(run).removeFilter();
    }
    
    public boolean containsKey(Run run){
        return testResults.containsKey(run);
    }
    
}
