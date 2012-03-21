
package com.redhat.engineering.jenkins.report.plugin;

import hudson.Extension;
import hudson.matrix.MatrixProject;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
@Extension
public class ReportPluginActionFactory extends TransientProjectActionFactory{

    /**
    * {@inheritDoc}
    */
    @Override
    public Collection<? extends Action> createFor(AbstractProject ap) {
	/* FIXME: this adds item for ALL matrix projects, which is what we do NOT
	 * want. Instead we want to enable it only for those project, that have 
	 * checked our buildStepDescriptor on configuration page (Publish reports...)
	 */
	ArrayList<Action> actions = new ArrayList<Action>();
        ReportPluginProjectAction newAction = new ReportPluginProjectAction(ap);
	actions.add((Action)newAction);
	
	/**
	* Test if project is matrix project
	*/
        if(ap instanceof MatrixProject){
	    actions.add(newAction);
	}
	return actions;
    }
    
}
