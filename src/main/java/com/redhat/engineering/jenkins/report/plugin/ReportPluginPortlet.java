/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.view.dashboard.DashboardPortlet;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author jrusnack
 */
public class ReportPluginPortlet extends DashboardPortlet {
        
        @DataBoundConstructor
	public ReportPluginPortlet(String name) {
            super(name);
	}
        
        @Extension
        public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

            @Override
            public String getDisplayName() {
                return Definitions.__DISPLAY_NAME;
            }
	}
        
}
