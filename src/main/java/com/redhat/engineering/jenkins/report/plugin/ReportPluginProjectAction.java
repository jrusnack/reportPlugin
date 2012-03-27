
package com.redhat.engineering.jenkins.report.plugin;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.JFreeChart;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginProjectAction implements Action{
    private final AbstractProject<?, ?> project;

    
    /**
	* Used to figure out if we need to regenerate the graphs or not.
	* Only used in newGraphNotNeeded() method. Key is the request URI and value
	* is the number of builds for the project.
	*/
    private transient Map<String, Integer> requestMap = new HashMap<String, Integer>();
    
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
    /**
    * Returns <code>true</code> if there is a graph to plot.
    *
    * @return Value for property 'graphAvailable'.
    */
   public boolean isGraphActive() {
      AbstractBuild<?, ?> build = getProject().getLastBuild();
      // in order to have a graph, we must have at least two points.
      int numPoints = 0;
      while (numPoints < 2) {
         if (build == null) {
            return false;
         }
         if (build.getAction(ReportPluginBuildAction.class) != null) {
            numPoints++;
         }
         build = build.getPreviousBuild();
      }
      return true;
   }
   
   
  public void doGraphMap(final StaplerRequest req,
           StaplerResponse rsp) throws IOException {
      if (newGraphNotNeeded(req, rsp)) {
         return;
      }

      final DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dataSetBuilder =
      new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>();

      //TODO: optimize by using cache
      populateDataSetBuilder(dataSetBuilder);
      new hudson.util.Graph(-1, getGraphWidth(), getGraphHeight()) {
         protected JFreeChart createGraph() {
           return GraphHelper.createChart(req, dataSetBuilder.build());
         }
      }.doMap(req, rsp);
   }
  
    /**
    * If number of builds hasn't changed and if checkIfModified() returns true,
    * no need to regenerate the graph. Browser should reuse it's cached image
    *
    * @param req
    * @param rsp
    * @return true, if new image does NOT need to be generated, false otherwise
    */
   private boolean newGraphNotNeeded(final StaplerRequest req,
         StaplerResponse rsp) {
      Calendar t = getProject().getLastCompletedBuild().getTimestamp();
      Integer prevNumBuilds = requestMap.get(req.getRequestURI());
      int numBuilds = getProject().getBuilds().size();

      //change null to 0
      prevNumBuilds = prevNumBuilds == null ? 0 : prevNumBuilds;
      if (prevNumBuilds != numBuilds) {
        requestMap.put(req.getRequestURI(), numBuilds);
      }

      if (requestMap.size() > 10) {
        //keep map size in check
        requestMap.clear();
      }

      if (prevNumBuilds == numBuilds && req.checkIfModified(t, rsp)) {
         /*
          * checkIfModified() is after '&&' because we want it evaluated only
          * if number of builds is different
          */
         return true;
      }
      return false;
   }
  
     protected void populateDataSetBuilder(DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dataset) {

      for (AbstractBuild<?, ?> build = getProject().getLastBuild();
               build != null; build = build.getPreviousBuild()) {
         ChartUtil.NumberOnlyBuildLabel label = new ChartUtil.NumberOnlyBuildLabel(build);
         ReportPluginBuildAction action = build.getAction(ReportPluginBuildAction.class);
         if (action != null) {
            dataset.add(action.getPassedTestCount(), "Passed", label);
            dataset.add(action.getFailedTestCount(), "Failed", label);
            dataset.add(action.getSkippedTestCount(), "Skipped", label);
         } else {
            //even if testng plugin wasn't run with this build,
            //we should add this build to the graph
            dataset.add(0, "Passed", label);
            dataset.add(0, "Failed", label);
            dataset.add(0, "Skipped", label);
         }
      }
   }
   
}
