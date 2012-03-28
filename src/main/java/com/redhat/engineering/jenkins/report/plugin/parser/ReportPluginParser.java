package com.redhat.engineering.jenkins.report.plugin.parser;

import com.redhat.engineering.jenkins.report.plugin.results.*;
import hudson.FilePath;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Parses testng result XMLs generated using org.testng.reporters.XmlReporter
 * into objects that are then used to display results in Jenkins.
 *
 * (For those trying to modify this class, pay attention to logging. We are using
 * two different loggers. If Build's {@link PrintStream} is not available, we log
 * using {@link Logger}. Also, logging is done only using the {@link #log(String)}
 * and {@link #log(Exception)} methods.)
 *
 * Note that instances of this class are not thread-safe to use!
 *
 * @author nullin
 */
public class ReportPluginParser {

   /** Prints the logs to the web server's console / log files */
   private static final Logger log = Logger.getLogger(ReportPluginParser.class.getName());
   public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
   public static XmlPullParserFactory PARSER_FACTORY;
   private final DateFormat dateFormat;

   /** Build's logger to print logs as part of build's console output */
   private PrintStream logger;

   /*
    * We maintain only a single TestResult for all <test>s with the same name
    */
   private Map<String, TestResult> testResultMap = new HashMap<String, TestResult>();
   /*
    * We maintain only a single ClassResult for all <class>s with the same fqdn
    */
   private Map<String, ClassResult> classResultMap = new HashMap<String, ClassResult>();
   private Map<String, List<String>> methodGroupMap = new HashMap<String, List<String>>();
   private RunTestResults finalResults;
   private List<TestResult> testList;
   private List<ClassResult> currentClassList;
   private List<MethodResult> currentMethodList;
   private List<String> currentMethodParamsList;
   private TestResult currentTest;
   private ClassResult currentClass;
   private String currentTestRunId;
   private MethodResult currentMethod;
   private XmlPullParser xmlPullParser;
   private ReportPluginParser.TAGS currentCDATAParent = ReportPluginParser.TAGS.UNKNOWN;
   private String currentMessage;
   private String currentShortStackTrace;
   private String currentFullStackTrace;
   private String currentGroupName;
   private String currentSuite;

   private enum TAGS {
      TESTNG_RESULTS, SUITE, TEST, CLASS, TEST_METHOD,
      PARAMS, PARAM, VALUE, EXCEPTION, UNKNOWN, MESSAGE,
      SHORT_STACKTRACE, FULL_STACKTRACE, GROUPS, GROUP, METHOD;

      public static ReportPluginParser.TAGS fromString(String val) {
         if (val == null) {
            return UNKNOWN;
         }
         val = val.toUpperCase().replace('-', '_');
         try {
            return ReportPluginParser.TAGS.valueOf(val);
         } catch (IllegalArgumentException e) {
            return UNKNOWN;
         }
      }
   }

   static {
      try {
         PARSER_FACTORY = XmlPullParserFactory.newInstance();
         PARSER_FACTORY.setNamespaceAware(true);
         PARSER_FACTORY.setValidating(false);
      } catch (XmlPullParserException e) {
         log.severe(e.toString());
      }
   }

   public ReportPluginParser() {
      this.dateFormat = new SimpleDateFormat(DATE_FORMAT);
   }

   public ReportPluginParser(PrintStream logger) {
      this();
      this.logger = logger;
   }

   /**
    * Parses the XML for relevant information
    *
    * @param paths	a file hopefully containing test related data in correct format
    * @param isMulticonf true if we are part of MatrixProject   
    * @return		results packed in MatrixRunTestResults if isMulticonf, 
    *			packed in FreeStyleTestResults otherwise
    *			
    */
   public RunTestResults parse(FilePath[] paths, boolean isMulticonf) {
      if (null == paths) {
         log("File paths not specified. paths var is null. Returning empty test results.");
         if (isMulticonf){
	    return new MatrixRunTestResults("");
	 } else{
	    // TODO: [freestyle] implement
	    return null;
	 }
      }

      if (isMulticonf){
	  finalResults = new MatrixRunTestResults(UUID.randomUUID().toString());
      } else{
	  // TODO: [freestyle] implement
	  finalResults = null;
      }
      
      
      for (FilePath path : paths) {
         File file = new File(path.getRemote());

         if (!file.isFile()) {
            log("'" + file.getAbsolutePath() + "' points to an invalid test report");
            continue; //move to next file
         } else {
            log("Processing '" + file.getAbsolutePath() + "'");
         }

         BufferedInputStream bufferedInputStream = null;
         try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            xmlPullParser = createXmlPullParser(bufferedInputStream);

            //some initial setup
            testList = new ArrayList<TestResult>();

            while (XmlPullParser.END_DOCUMENT != xmlPullParser.nextToken()) {
               ReportPluginParser.TAGS tag = ReportPluginParser.TAGS.fromString(xmlPullParser.getName());
               int eventType = xmlPullParser.getEventType();

               switch (eventType) {
                  //all opening tags
                  case XmlPullParser.START_TAG:
                     switch (tag) {
                        case SUITE:
                           startSuite(get("name"));
                           break;
                        case GROUPS:
                           startGroups();
                           break;
                        case GROUP:
                           startGroup(get("name"));
                           break;
                        case METHOD:
                           startGroupMethod(get("class"), get("name"));
                           break;
                        case TEST:
                           startTest(get("name"));
                           break;
                        case CLASS:
                           startClass(get("name"));
                           break;
                        case TEST_METHOD:
                           startTestMethod(get("name"), get("test-instance-name"),
                                    get("status"), get("description"),
                                    get("duration-ms"), get("started-at"),
                                    get("is-config"));
                           break;
                        case PARAMS:
                           startMethodParameters();
                           currentCDATAParent = ReportPluginParser.TAGS.PARAMS;
                           break;
                        case EXCEPTION:
                           startException();
                           break;
                        case MESSAGE:
                           currentCDATAParent = ReportPluginParser.TAGS.MESSAGE;
                           break;
                        case SHORT_STACKTRACE:
                           currentCDATAParent = ReportPluginParser.TAGS.SHORT_STACKTRACE;
                           break;
                        case FULL_STACKTRACE:
                           currentCDATAParent = ReportPluginParser.TAGS.FULL_STACKTRACE;
                           break;
                     }
                     break;
                  // all closing tags
                  case XmlPullParser.END_TAG:
                     switch (tag) {
                        case SUITE:
                           finishSuite();
                           break;
                        case GROUP:
                           finishGroup();
                           break;
                        case METHOD:
                           finishGroupMethod();
                           break;
                        case TEST:
                           finishTest();
                           break;
                        case CLASS:
                           finishclass();
                           break;
                        case TEST_METHOD:
                           finishTestMethod();
                           break;
                        case PARAMS:
                           finishMethodParameters();
                           currentCDATAParent = ReportPluginParser.TAGS.UNKNOWN;
                           break;
                        case EXCEPTION:
                           finishException();
                           break;
                        case MESSAGE:
                        case SHORT_STACKTRACE:
                        case FULL_STACKTRACE:
                           currentCDATAParent = ReportPluginParser.TAGS.UNKNOWN;
                           break;
                     }
                     break;
                  // all cdata reading
                  case XmlPullParser.CDSECT:
                     handleCDATA();
                     break;
               }
            }
            finalResults.addUniqueTests(testList);
         } catch (XmlPullParserException e) {
            log("Failed to parse XML: " + e.getMessage());
            log(e);
         } catch (FileNotFoundException e) {
           log("Failed to find XML file");
           log(e);
         } catch (IOException e) {
           log(e);
         } finally {
            try {
               if (bufferedInputStream != null) {
                  bufferedInputStream.close();
               }
            } catch (IOException e) {
               log(e);
            }
         }
      }

      //tally up the results properly before returning
      finalResults.tally();
      return finalResults;
   }

   private void startGroupMethod(String className, String methodName)
   {
      String key = className + "|" + methodName;
      List<String> groups = methodGroupMap.get(key);
      if (groups == null) {
         groups = new ArrayList<String>(3);
         groups.add(currentGroupName);
         methodGroupMap.put(key, groups);
      } else {
         groups.add(currentGroupName);
      }
   }

   private void finishGroupMethod()
   {
      // nothing to do
   }

   private void startGroup(String groupName)
   {
      currentGroupName = groupName;
   }

   private void finishGroup()
   {
      currentGroupName = null;
   }

   private void startGroups()
   {
      methodGroupMap = new HashMap<String, List<String>>();
   }

   private void startSuite(String name)
   {
      currentSuite = name;
   }

   private void finishSuite()
   {
      methodGroupMap.clear();
      currentSuite = null;
   }

   private void startException()
   {
      // do nothing (here for symmetry)
   }

   private void finishException()
   {
      if (currentShortStackTrace == null && currentFullStackTrace == null) {
         log("Something is wrong with TestNG result XML. "
                    + "Didn't find stacktraces for the exception.");
         return;
      }
      //FIXME: research Exception in TestNG xml and implement MethodResultException class
      MethodResultException mrEx = new MethodResultException(currentMessage,
               currentShortStackTrace, currentFullStackTrace);
      currentMethod.setException(mrEx);

      mrEx = null;
      currentMessage = null;
      currentShortStackTrace = null;
      currentFullStackTrace = null;
   }

   private void startMethodParameters()
   {
      currentMethodParamsList = new ArrayList<String>();
   }

   private void finishMethodParameters()
   {
      currentMethod.setParameters(currentMethodParamsList);
      currentMethodParamsList = null;
   }

   private void handleCDATA()
   {
      switch (currentCDATAParent) {
         case PARAMS:
            currentMethodParamsList.add(xmlPullParser.getText());
            break;
         case MESSAGE:
            currentMessage = xmlPullParser.getText();
            break;
         case FULL_STACKTRACE:
            currentFullStackTrace = xmlPullParser.getText();
            break;
         case SHORT_STACKTRACE:
            currentShortStackTrace = xmlPullParser.getText();
            break;
         case UNKNOWN:
            //do nothing
      }
   }

   private void startTestMethod(String name,
            String testInstanceName,
            String status,
            String description,
            String duration,
            String startedAt,
            String isConfig)
   {
      Date startedAtDate;
      try {
         startedAtDate = this.dateFormat.parse(startedAt);
      } catch (ParseException e) {
         log("Unable to parse started-at value: " + startedAt);
         startedAtDate = null;
      }
      currentMethod = new MethodResult(name, status, description, duration,
         startedAtDate, isConfig, currentTestRunId, currentTest.getName(),
         currentSuite, testInstanceName);
      List<String> groups = methodGroupMap.get(currentClass.getName() + "|" + name);
      if (groups != null) {
         currentMethod.setGroups(groups);
      }
   }

   private void finishTestMethod()
   {
      updateTestMethodLists(currentMethod);
      // add to test methods list for each class
      currentMethodList.add(currentMethod);

      currentMethod = null;
   }

   private void startClass(String name)
   {
      if (classResultMap.containsKey(name)) {
         currentClass = classResultMap.get(name);
      } else {
         currentClass = new ClassResult(name);
         classResultMap.put(name, currentClass);
      }
      currentMethodList = new ArrayList<MethodResult>();
      //reset for each class
      currentTestRunId = UUID.randomUUID().toString();
   }

   private void finishclass()
   {
      currentClass.addTestMethods(currentMethodList);
      currentClassList.add(currentClass);

      currentMethodList = null;
      currentClass = null;
      currentTestRunId = null;
   }

   private void startTest(String name)
   {
      if (testResultMap.containsKey(name)) {
         currentTest = testResultMap.get(name);
      } else {
         currentTest = new TestResult(name);
         testResultMap.put(name, currentTest);
      }
      currentClassList = new ArrayList<ClassResult>();
   }

   private void finishTest()
   {
      currentTest.addClassList(currentClassList);
      testList.add(currentTest);

      currentClassList = null;
      currentTest = null;
   }

   private void updateTestMethodLists(MethodResult testMethod) {
      if (testMethod.isConfig()) {
         if ("FAIL".equals(testMethod.getStatus())) {
            finalResults.getFailedConfigs().add(testMethod);
         } else if ("SKIP".equals(testMethod.getStatus())) {
            finalResults.getSkippedConfigs().add(testMethod);
         }
      } else {
         if ("FAIL".equals(testMethod.getStatus())) {
            finalResults.getFailedTests().add(testMethod);
         } else if ("SKIP".equals(testMethod.getStatus())) {
            finalResults.getSkippedTests().add(testMethod);
         } else if ("PASS".equals(testMethod.getStatus())) {
            finalResults.getPassedTests().add(testMethod);
         }
      }
   }

   private String get(String attr)
   {
      return xmlPullParser.getAttributeValue(null, attr);
   }

   private XmlPullParser createXmlPullParser(BufferedInputStream
            bufferedInputStream) throws XmlPullParserException {
      if (PARSER_FACTORY == null) {
         throw new XmlPullParserException("XML Parser Factory has not been initiallized properly");
      }
      XmlPullParser xmlPullParser = PARSER_FACTORY.newPullParser();
      xmlPullParser.setInput(bufferedInputStream, null);
      return xmlPullParser;
   }

   private void log(String str) {
      if (logger != null) {
         logger.println(str);
      } else {
         log.fine(str);
      }
   }

   private void log(Exception ex) {
      if (logger != null) {
         ex.printStackTrace(logger);
      } else {
         log.severe(ex.toString());
      }
   }
}