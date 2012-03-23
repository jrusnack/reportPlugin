/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.results.MatrixBuildTestResults;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import java.io.Serializable;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginBuildAction implements Action, Serializable{
    MatrixBuildTestResults results;
    AbstractBuild<?, ?> build;
    
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
    
}
