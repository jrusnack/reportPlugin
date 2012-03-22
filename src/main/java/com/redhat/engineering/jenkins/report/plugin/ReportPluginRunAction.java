/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.results.MatrixRunTestResults;
import hudson.matrix.MatrixRun;
import java.io.PrintStream;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
class ReportPluginRunAction {

    //TODO: implement ReportPluginRunAction.loadResults
    static MatrixRunTestResults loadResults(MatrixRun mrun, PrintStream logger) {
	throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
