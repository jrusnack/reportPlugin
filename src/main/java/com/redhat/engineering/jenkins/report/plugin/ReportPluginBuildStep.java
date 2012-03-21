/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildStep;
import hudson.tasks.BuildStepMonitor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginBuildStep{

    public boolean prebuild(AbstractBuild<?, ?> ab, BuildListener bl) {
	return true;
    }

    public boolean perform(AbstractBuild<?, ?> ab, Launcher lnchr, BuildListener bl) throws InterruptedException, IOException {
	return true;
    }

    public Action getProjectAction(AbstractProject<?, ?> ap) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> ap) {
	Collection<Action> actions = new ArrayList<Action>();
	//actions.add(new ReportPluginProjectAction());
	return actions;
    }

    public BuildStepMonitor getRequiredMonitorService() {
	return BuildStepMonitor.NONE;
    }
    
}
