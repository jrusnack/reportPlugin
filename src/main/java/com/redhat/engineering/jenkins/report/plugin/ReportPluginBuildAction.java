/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.testparser.results.MatrixBuildTestResults;
import com.redhat.engineering.jenkins.testparser.results.MatrixRunTestResults;
import hudson.matrix.MatrixRun;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import java.io.PrintStream;
import java.io.Serializable;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginBuildAction implements Action, Serializable{
    MatrixBuildTestResults results;
    AbstractBuild<?, ?> build;
    
   /*
    * Cache test counts to speed up loading of graphs
    */
   private transient int passedTestCount;
   private transient int failedTestCount;
   private transient int skippedTestCount;
    
    public ReportPluginBuildAction(AbstractBuild<?, ?> build, MatrixBuildTestResults results){
	super();
	this.results = results;
	this.build = build;	
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
    
    //FIXME: implement
    static MatrixRunTestResults loadResults(MatrixRun mrun, PrintStream logger) {
	throw new UnsupportedOperationException("Not yet implemented");
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
    
}
