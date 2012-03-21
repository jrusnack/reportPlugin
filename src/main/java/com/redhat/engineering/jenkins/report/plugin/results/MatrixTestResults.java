
package com.redhat.engineering.jenkins.report.plugin.results;

import hudson.matrix.MatrixRun;

/**
 * This is class that stores mapping Matrix Run -> Test Results
 * 
 * FIXME: handle change of configuration (add/remove axis ...)
 * 
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class MatrixTestResults {


    public MatrixTestResults(String string) {
	throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public boolean addMatrixTestResults(MatrixRun mrun, BuildTestResults results){
	//TODO: implement
	return true;
    }
    
}
