/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.parser.ResultsParser;
import com.redhat.engineering.jenkins.report.plugin.results.TestResults;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginBuildAction implements Action, Serializable{
    private final AbstractBuild<?, ?> build;
    private transient WeakReference<TestResults> testResults;
    private transient int passedTestCount;
    private transient int failedTestCount;
    private transient int skippedTestCount;

    public ReportPluginBuildAction(AbstractBuild<?, ?> build, TestResults results){
	this.build = build;
	results.setOwner(this.build);
	this.testResults = new WeakReference<TestResults>(results);

	//initialize the cached values when TestNGBuildAction is instantiated
	this.passedTestCount = results.getPassedTestCount();
	this.failedTestCount = results.getFailedTestCount();
	this.skippedTestCount = results.getSkippedTestCount();
    }
    
    public String getIconFileName() {
	return Definitions.__ICON_FILE_NAME;
    }

    public String getDisplayName() {
	return Definitions.__DISPLAY_NAME;
    }

    public String getUrlName() {
	return Definitions.__URL_NAME;
    }
    
    public int getPassedTestCount() {
	return this.passedTestCount;
    }

    public int getFailedTestCount() {
	return this.failedTestCount;
    }

    public int getSkippedTestCount() {
	return this.skippedTestCount;
    }
    
    static TestResults loadResults(AbstractBuild<?, ?> owner, PrintStream logger)
   {
      FilePath testngDir = ReportPluginPublisher.getReportDirectory(owner);
      FilePath[] paths = null;
      try {
         paths = testngDir.list("testng-results*.xml");
      } catch (Exception e) {
         //do nothing
      }

      TestResults tr = null;
      if (paths == null) {
        tr = new TestResults("");
        tr.setOwner(owner);
        return tr;
      }

      ResultsParser parser = new ResultsParser(logger);
      TestResults result = parser.parse(paths);
      result.setOwner(owner);
      return result;
   }

    public TestResults getResults() {
	
        TestResults tr = testResults.get();
        if (tr == null) {
           testResults = new WeakReference<TestResults>(loadResults(getBuild(), null));
          return testResults.get();
        } else {
          return tr;
        }
    }

    public AbstractBuild<?, ?> getBuild() {
	return build;
    }
    
}
