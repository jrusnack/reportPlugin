/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.ProminentProjectAction;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginProjectAction implements ProminentProjectAction {
    private boolean escapeTestDescp;
    private final boolean escapeExceptionMsg;
    private final AbstractProject<?, ?> project;

    public ReportPluginProjectAction(AbstractProject<?, ?> project,
	    boolean escapeTestDescp, boolean escapeExceptionMsg){
	this.project = project;
	this.escapeExceptionMsg = escapeExceptionMsg;
	this.escapeTestDescp = escapeTestDescp;
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

    public boolean getEscapeTestDescp() {
	return escapeTestDescp;
    }

    public boolean getEscapeExceptionMsg() {
	return escapeExceptionMsg;
    }
    
    public AbstractProject<?, ?> getProject() {
	return project;
    }
}
