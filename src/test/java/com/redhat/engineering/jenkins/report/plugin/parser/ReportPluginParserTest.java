/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin.parser;

import com.redhat.engineering.jenkins.report.plugin.results.PackageResult;
import com.redhat.engineering.jenkins.report.plugin.results.RunTestResults;
import com.redhat.engineering.jenkins.report.plugin.results.TestResults;
import hudson.FilePath;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginParserTest {
    
    public ReportPluginParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
   public void testTestngXmlWithExistingResultXml() {
      String filename = "sample-testng-results.xml";
      URL resource = ReportPluginParserTest.class.getClassLoader().getResource(filename);
      junit.framework.Assert.assertNotNull(resource);
      TestResults results = (TestResults)getResults(resource.getFile());
      junit.framework.Assert.assertFalse("Collection shouldn't have been empty", results.getTestList().isEmpty());
   }

   @Test
   public void testTestngXmlWithSameTestNameDiffSuites() {
      String filename = "testng-results-same-test.xml";
      URL resource = ReportPluginParserTest.class.getClassLoader().getResource(filename);
      junit.framework.Assert.assertNotNull(resource);
      TestResults results = (TestResults)getResults(resource.getFile());
      junit.framework.Assert.assertFalse("Collection shouldn't have been empty", results.getTestList().isEmpty());
      junit.framework.Assert.assertEquals(2, results.getTestList().size());
      results.tally();
      junit.framework.Assert.assertEquals(1, results.getPackageNames().size());
      junit.framework.Assert.assertEquals(3, results.getPackageMap().values().iterator().next().getClassList().size());
      junit.framework.Assert.assertEquals(4, results.getPassedTestCount());
      junit.framework.Assert.assertEquals(4, results.getPassedTests().size());
   }

   @Test
   public void testTestngXmlWithExistingResultXmlGetsTheRightDurations() {
      String filename = "sample-testng-dp-result.xml";
      URL resource = ReportPluginParserTest.class.getClassLoader().getResource(filename);
      junit.framework.Assert.assertNotNull(resource);
      TestResults results = (TestResults)getResults(resource.getFile());
      junit.framework.Assert.assertFalse("Collection shouldn't have been empty", results.getTestList().isEmpty());

      // This test assumes that there is only 1 package in
      // sample-testng-dp-result that contains tests that add to 12 ms
      results.tally();
      Map<String, PackageResult> packageResults = results.getPackageMap();
      for(PackageResult result: packageResults.values()) {
        junit.framework.Assert.assertEquals("org.farshid", result.getName());
        junit.framework.Assert.assertEquals(12, result.getDuration());
      }
   }

   @Test
   public void testTestngXmlWithNonExistingResultXml() {
      String filename = "/invalid/path/to/file/new-test-result.xml";
      TestResults results = (TestResults)getResults(filename);
      junit.framework.Assert.assertTrue("Collection should have been empty. Number of results : "
               + results.getTestList().size(), results.getTestList().isEmpty());
   }

   @Test
   public void parseTestNG() {
      ClassLoader cl = ReportPluginParserTest.class.getClassLoader();
      TestResults results = (TestResults)getResults(cl.getResource("testng-results-testng.xml").getFile());
      results.tally();
   }

   @Test
   public void testParseEmptyException() {
      ClassLoader cl = ReportPluginParserTest.class.getClassLoader();
      TestResults results = (TestResults)getResults(cl.getResource("sample-testng-empty-exp.xml").getFile());
      results.tally();
      junit.framework.Assert.assertEquals(1, results.getPassedTestCount());
   }

   private RunTestResults getResults(String filename) {
      ReportPluginParser parser = new ReportPluginParser();
      FilePath[] filePaths = new FilePath[1];
      filePaths[0] = new FilePath(new File(filename));
      return parser.parse(filePaths, true);
   }

   @Test
   public void testDateParser() throws ParseException {
      //example of date format used in testng report
      String dateString = "2010-07-20T11:49:17Z";
      SimpleDateFormat sdf = new SimpleDateFormat(ReportPluginParser.DATE_FORMAT);
      sdf.parse(dateString);
   }
}
