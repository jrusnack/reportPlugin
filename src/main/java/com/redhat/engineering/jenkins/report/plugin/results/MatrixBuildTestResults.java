
package com.redhat.engineering.jenkins.report.plugin.results;

import hudson.matrix.MatrixRun;
import hudson.model.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is class that stores mapping Matrix Run -> Test Results
 * 
 * TODO: handle change of configuration (add/remove axis ...)
 * 
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class MatrixBuildTestResults extends TestResults {
    private Map<String, MatrixRunTestResults> results = new HashMap<String, MatrixRunTestResults>();
    private int failedConfigCount;
    private int skippedConfigCount;
    private List<MethodResult> failedConfigurationMethods = new ArrayList<MethodResult>();
    private List<MethodResult> skippedConfigurationMethods = new ArrayList<MethodResult>();

    public MatrixBuildTestResults(String name) {
	super(name);
	failedConfigCount = 0;
	skippedConfigCount = 0;
    }
    
    /**
     * Add test results of child matrix run to this build`s results. 
     * Duplicates are not added.
     * 
     * @param mrun	matrix run to which results correspond to
     * @param results	
     * @return		false if this run is already mapped to results
     */
    public boolean addMatrixRunTestResults(MatrixRun mrun, MatrixRunTestResults results){
	
	// test if already added
	if(this.results.get(mrun.toString()) == null){
	    this.results.put(mrun.getDisplayName(), results);
	    // FIXME: update getFailedConfigCount
	    // FIXME: is owner really the one we should set stability?
	    if (results.getFailedTestCount() > 0){
		owner.setResult(Result.UNSTABLE);
	    } else {
		owner.setResult(Result.SUCCESS);
	    }
	    return true;
	}
	return false;
    }

    public int getFailedConfigCount() {
	return failedConfigCount;
    }
    
    public int getSkippedConfigCount(){
	return skippedConfigCount;
    }

    @Override
    public boolean isMatrixBuildTestResult() {
	return true;
    }

    public List<MethodResult> getFailedConfigs() {
	return failedConfigurationMethods;
    }

    public List<MethodResult> getSkippedConfigs() {
	return skippedConfigurationMethods;
    }

    
}
