/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.testparser.results.Filter;
import com.redhat.engineering.jenkins.testparser.results.MatrixBuildTestResults;
import hudson.matrix.Combination;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.util.RunList;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletException;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */

// FIXME: implement whole class and crete jellys
public class ReportPluginBuildAction implements Action, Serializable{
    MatrixBuildTestResults results;
    AbstractBuild<?, ?> build;
    ReportPluginProjectAction projectAction;
    
    
    public ReportPluginBuildAction(AbstractBuild<?, ?> build, 
	    MatrixBuildTestResults results, ReportPluginProjectAction project){
	this.results = results;
	this.build = build;
	this.projectAction = project;	
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
    
    public AbstractBuild getBuild() {
        return build;
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
    
    public boolean isCombinationChecked(Combination combination){
        return projectAction.isCombinationChecked(combination);
    }
            
    public boolean combinationExists( AbstractProject ap, Combination c){
        return projectAction.combinationExists(ap, c);
    }
    
    public boolean isGraphActive() {
        return projectAction.isGraphActive();
    }
    
    public void doGraph(final StaplerRequest req,
			StaplerResponse rsp) throws IOException {
        projectAction.doGraph(req, rsp);
    }
    
    public void doConfigSubmit(StaplerRequest req, StaplerResponse rsp) throws ServletException,
            IOException, InterruptedException {
        projectAction.doConfigSubmit(req, rsp);
    }
    
    public int getBuildsRecentNumber(){
        return projectAction.getBuildsRecentNumber();
    }
    
    public boolean getBuildsAllChecked(){
        return projectAction.getBuildsAllChecked();
    }
    
    public boolean getBuildsRecentChecked(){
        return projectAction.getBuildsRecentChecked();
    }
    
    public boolean getBuildsIntervalChecked(){
        return projectAction.getBuildsIntervalChecked();
    }
    
    public boolean getMatrixChecked(){
        return projectAction.getMatrixChecked();
    }
    
    public boolean getCombinationFilterChecked(){
        return projectAction.getCombinationFilterChecked();
    }
    
    public RunList<?> getAllBuilds(){
        return projectAction.getAllBuilds();
    }
    
    public long getFirstSelBuildTimestamp() {
        return projectAction.getFirstSelBuildTimestamp();
    }
    
    public long getLastSelBuildTimestamp() {
        return projectAction.getLastSelBuildTimestamp();
    }
    
    public String getCombinationFilter(){
        return projectAction.getCombinationFilter();
    }
    
    public String getAxes(){
        return projectAction.getAxes();
    }
    
}
