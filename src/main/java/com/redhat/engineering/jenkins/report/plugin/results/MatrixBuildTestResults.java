
package com.redhat.engineering.jenkins.report.plugin.results;

import hudson.matrix.MatrixRun;
import java.util.HashMap;
import java.util.Map;

/**
 * This is class that stores mapping Matrix Run -> Test Results
 * 
 * TODO: handle change of configuration (add/remove axis ...)
 * 
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class MatrixBuildTestResults extends BaseResult {
    private Map<String, MatrixRunTestResults> results = new HashMap<String, MatrixRunTestResults>();

    public MatrixBuildTestResults(String name) {
	super(name);
    }
    
    /**
     * Add test results of child matrix run to this build`s results. 
     * Duplicates are not added.
     * 
     * @param mrun	matrix run to which results correspond to
     * @param results	
     * @return		false if this run is already mapped to results
     */
    public boolean addMatrixTestResults(MatrixRun mrun, MatrixRunTestResults results){
	
	if(this.results.get(mrun) == null){
	    this.results.put(mrun.getDisplayName(), results);
	    return true;
	}
	return false;
    }
    
}
