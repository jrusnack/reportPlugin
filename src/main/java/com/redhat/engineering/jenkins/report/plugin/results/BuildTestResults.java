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
public class BuildTestResults {
    
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
