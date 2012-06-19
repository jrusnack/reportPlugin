/*
 * Copyright (C) 2012 jrusnack
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.results.Filter;
import com.redhat.engineering.jenkins.report.plugin.results.MatrixBuildTestResults;
import hudson.model.Action;
import hudson.model.Run;
import java.io.Serializable;

/**
 *
 * @author jrusnack
 */
public class ReportPluginBuildAction implements Action, Serializable {
    
    private MatrixBuildTestResults testResults;
    
    public ReportPluginBuildAction (MatrixBuildTestResults testResults){
        this.testResults = testResults;
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return null;
    }
    
    public MatrixBuildTestResults getTestResults (){
        return testResults;
    }
    
    public int getPassedTestCount(Run run) {
        return testResults.getPassedTestCount();
    }
    
    public int getFailedTestCount(Run run) {
        return testResults.getFailedTestCount();
    }
    
    public int getSkippedTestCount(Run run) {
        return testResults.getSkippedTestCount();
    }
    
    public void addFilter(Run run, Filter filter){
        testResults.addFilter(filter);
    }
    
    public void removeFilter(Run run){
        testResults.removeFilter();
    }
    
}
