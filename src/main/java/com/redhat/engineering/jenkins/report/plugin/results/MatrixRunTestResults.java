/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin.results;

import hudson.model.AbstractBuild;
import java.util.*;
import org.kohsuke.stapler.export.Exported;

/**
 * TODO: Fix whole class (it is analogue to TestResults in TestNG plugin)
 * 
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class MatrixRunTestResults extends BaseResult {
    private List<MethodResult> passedTests = new ArrayList<MethodResult>();
    private List<MethodResult> failedTests = new ArrayList<MethodResult>();
    private List<MethodResult> skippedTests = new ArrayList<MethodResult>();
    private List<MethodResult> failedConfigurationMethods = new ArrayList<MethodResult>();
    private List<MethodResult> skippedConfigurationMethods = new ArrayList<MethodResult>();
    private int totalTestCount;
    private List<TestResult> testList = new ArrayList<TestResult>();
    private long duration;
    private int passedTestCount;
    private int failedTestCount;
    private int skippedTestCount;
    private int failedConfigurationMethodsCount;
    private int skippedConfigurationMethodsCount;
    private Map<String, PackageResult> packageMap = new HashMap<String, PackageResult>();
    private AbstractBuild<?, ?> owner;
    
    public MatrixRunTestResults(String name){
	super(name);
    }
    
    public List<MethodResult> getFailedTests() {
	return failedTests;
    }

    public List<MethodResult> getPassedTests() {
	return passedTests;
    }

    public List<MethodResult> getSkippedTests() {
	return skippedTests;
    }

    public List<MethodResult> getFailedConfigs() {
	return failedConfigurationMethods;
    }

    public List<MethodResult> getSkippedConfigs() {
	return skippedConfigurationMethods;
    }

    public List<TestResult> getTestList() {
	return testList;
    }

    @Exported(name = "total")
    public int getTotalTestCount() {
	return totalTestCount;
    }

    @Exported
    public long getDuration() {
	return duration;
	}

    public int getPassedTestCount() {
	return passedTestCount;
    }

    @Exported(name = "fail")
    public int getFailedTestCount() {
	return failedTestCount;
    }

    @Exported(name = "skip")
    public int getSkippedTestCount() {
	return skippedTestCount;
    }

    @Exported(name = "fail-config")
    public int getFailedConfigCount() {
	return failedConfigurationMethodsCount;
    }

    @Exported(name = "skip-config")
    public int getSkippedConfigCount() {
	return skippedConfigurationMethodsCount;
    }

    @Exported(name = "package")
    public Collection<PackageResult> getPackageList() {
	return packageMap.values();
    }

    public Map<String, PackageResult> getPackageMap() {
	return packageMap;
    }

    public Set<String> getPackageNames() {
	return packageMap.keySet();
    }

    /**
    * Adds only the <test>s that already aren't part of the list
    * @param classList
    */
    public void addUniqueTests(List<TestResult> testList) {
	Set<TestResult> tmpSet = new HashSet<TestResult>(this.testList);
	tmpSet.addAll(testList);
	this.testList = new ArrayList<TestResult>(tmpSet);
    }

    public void setOwner(AbstractBuild<?, ?> owner) {
	this.owner = owner;
	for (TestResult _test : testList) {
	    _test.setOwner(owner);
	}
	for (PackageResult pkg : packageMap.values()) {
	    pkg.setOwner(owner);
	}
    }
}
