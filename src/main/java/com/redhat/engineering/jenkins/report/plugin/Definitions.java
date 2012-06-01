
package com.redhat.engineering.jenkins.report.plugin;

/**
*
* TODO: check serializable and other interfaces that might be implemented by our classes
* TODO: check private/public permissions of methods 
* TODO: optimize filtering - filter added to each action => ineffective
* 
* @author Jan Rusnacko (jrusnack at redhat.com)
*/
public class Definitions {
    
    public static final String __DISPLAY_NAME = "Report Plugin";
    public static final String __ICON_FILE_NAME = "/plugin/ReportPlugin/images/icon.png";
    public static final String __URL_NAME = "report-plugin";    
    public static final String __PREFIX = "test-results-";
    
    public static final String __DASHBOARD_PASSED = "Passed";
    public static final String __DASHBOARD_FAILED = "Failed";
    public static final String __DASHBOARD_SKIPPED = "Skipped";
    public static final String __DASHBOARD_PORTLET = "Report Plugin Trend Chart";
    
    public static final String __DASHBOARD_DATE = "Date";
    public static final String __DASHBOARD_COUNT = "Count";
}