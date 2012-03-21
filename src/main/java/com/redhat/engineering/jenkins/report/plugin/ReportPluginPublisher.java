
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.results.MatrixTestResults;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.matrix.MatrixRun;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginPublisher extends Recorder{
    private final String reportLocationPattern;

    
   /*
    * Get location of reports from project configuration page
    */
    @DataBoundConstructor
    public ReportPluginPublisher(String reportLocationPattern){
	this.reportLocationPattern = reportLocationPattern;
    }
    
    /**
     * Declares the scope of the synchronization monitor we expect from outside.
     * 
     * STEP = Executed only after the same step in the previous build is completed.
     * NONE = No external synchronization is performed on this build step.
     */
    public BuildStepMonitor getRequiredMonitorService() {
	//TODO: check if STEP is really necessary
	return BuildStepMonitor.STEP;
    }
    
    
    /**
     * Locates, checks and saves reports after build finishes, initializes 
     * BuildAction with results and adds it to build actions
     * 
     * @param build	    
     * @param launcher	    
     * @param listener	    
     * @return		    true if build can continue
     * @throws InterruptedException
     * @throws IOException 
     */
    @Override	    
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher,
	BuildListener listener) throws InterruptedException, IOException{
	
	/* Only for matrix projects now
	 * TODO: add also for other types of build (even though already implemented
	 * in TestNG plugin and JUnit publish)
	 */
	if(!(build instanceof MatrixBuild)){
	    return false;
	}
	
	PrintStream logger = listener.getLogger();
	logger.println("[Report Plugin] Report files processing: START");
	
	/*
	 * MatrixTestResults will store mapping matrix run -> test results
	 */
	MatrixTestResults results = new MatrixTestResults("");
	
	/*
	 * Iterate over all runs in matrix build 
	 */
	MatrixBuild mbuild = (MatrixBuild) build;
	for(MatrixRun mrun: mbuild.getRuns()){
	    logger.println("[Report Plugin] Starting to process Matrix Run.");
	    logger.println("[Report Plugin] Looking for results reports in workspace"
		+ " using pattern: " + reportLocationPattern);
	    
	    //TODO: locate results 
	    FilePath[] paths = locateReports(build.getWorkspace(), reportLocationPattern);
	    if (paths.length == 0) {
		logger.println("Did not find any matching files.");
		//build can still continue
		return true;
	    }
	    
	    /*
	    * filter out the reports based on timestamps. See JENKINS-12187
	    */
	    //TODO: implement report filtering based on timestamps
	    paths = checkReports(build, paths, logger);
	    
	    //TODO: implement saving reports 
	    boolean filesSaved = saveReports(getReportDir(mrun), paths, logger);
	    if (!filesSaved) {
		logger.println("Failed to save TestNG XML reports");
		return true;
	    }
	    
	    try {
		results.addMatrixTestResults(mrun, ReportPluginBuildAction.loadResults(mrun, logger));
	    } catch (Throwable t) {
		/*
		* don't fail build if parser barfs, only 
		* print out the exception to console.
		*/
		t.printStackTrace(logger);
	    } 
	    
	    if (results.getFailedConfigCount() > 0 || results.getFailedTestCount() > 0) {
		mrun.setResult(Result.UNSTABLE);
	    } 
	    
	    if(results.getResults(mrun).getTestList().getSize <= 0){
		logger.println("[Report Plugin] Found matching files but did not find any reports.");
		return true;
	    }
	    logger.println("[Report Plugin] Finished processing Matrix Run.");
	}
	
	ReportPluginBuildAction action = new ReportPluginBuildAction(build, results);
	mbuild.getActions().add(action);
	
	logger.println("[Report Plugin] Report Processing: FINISH");
	return true;
    }

    private FilePath[] locateReports(FilePath workspace, String reportLocationPattern) {
	throw new UnsupportedOperationException("Not yet implemented");
    }
    
    /**
     * Filter out the reports based on timestamps. Those with timestamp earlier 
     * than start of build are to be ignored. See JENKINS-12187
     */
    static FilePath[] checkReports(AbstractBuild<?,?> build, FilePath[] paths,
            PrintStream logger){
	
	//TODO: implement checkReports
	return paths;
    }
    
    /**
    * Gets the directory to store report files
    */
    static FilePath getReportDir(AbstractBuild<?,?> build) {
	return new FilePath(new File(build.getRootDir(), "report-plugin"));
    }

    private boolean saveReports(FilePath reportDir, FilePath[] paths, PrintStream logger) {
	throw new UnsupportedOperationException("Not yet implemented");
    }
    
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<hudson.tasks.Publisher> {

      
	@Override
	public boolean isApplicable(Class<? extends AbstractProject> project) {
	    if(project instanceof MatrixProject){
		return true;
	    }
	    return false;
	}

	@Override
	public String getDisplayName() {
	    return "Publish " + Definitions.__DISPLAY_NAME + " results";
	}
       
	// Invoked when global configuration page is submitted
	@Override
	public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
	    save();
	    return super.configure(req, formData);
	}
   }
}
