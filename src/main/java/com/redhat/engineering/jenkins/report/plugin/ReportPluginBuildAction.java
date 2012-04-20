/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.testparser.Parser;
import com.redhat.engineering.jenkins.testparser.results.Filter;
import com.redhat.engineering.jenkins.testparser.results.MatrixBuildTestResults;
import com.redhat.engineering.jenkins.testparser.results.MatrixRunTestResults;
import com.redhat.engineering.jenkins.testparser.results.TestResults;
import hudson.FilePath;
import hudson.matrix.MatrixRun;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.tasks.Publisher;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginBuildAction implements Action, Serializable{
    MatrixBuildTestResults results;
    AbstractBuild<?, ?> build;
    ReportPluginProjectAction project;
    
    
    public ReportPluginBuildAction(AbstractBuild<?, ?> build, 
	    MatrixBuildTestResults results, ReportPluginProjectAction project){
	this.results = results;
	this.build = build;
	this.project = project;	
	results.setOwner(this.build);
    }
    

    public MatrixBuildTestResults getBuildResults(){
	return results;
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
	// TODO: speed up via caching
	// return this.passedTestCount;
	return results.getPassedTestCount();
    }

    public int getFailedTestCount() {
	// TODO: speed up via caching
	//return this.failedTestCount;
	return results.getFailedTestCount();
    }

    public int getSkippedTestCount() {
	// TODO: speed up via caching
	//return this.skippedTestCount;
	return results.getSkippedTestCount();
    }
    
    
    public void addFilter(Filter filter){
	results.addFilter(filter);
    }
    
    public void removeFilter(){
	results.removeFilter();
    }
}
