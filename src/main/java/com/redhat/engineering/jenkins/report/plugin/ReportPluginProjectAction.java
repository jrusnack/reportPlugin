
package com.redhat.engineering.jenkins.report.plugin;

import hudson.model.AbstractProject;
import hudson.model.Action;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginProjectAction implements Action{
    private final AbstractProject<?, ?> project;

    public ReportPluginProjectAction(AbstractProject<?, ?> project){
	this.project = project;
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
    
    public AbstractProject<?, ?> getProject() {	
	return project;
    }
    
}
