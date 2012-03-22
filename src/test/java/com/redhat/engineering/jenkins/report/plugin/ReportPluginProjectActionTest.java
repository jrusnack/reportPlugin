/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import hudson.model.FreeStyleProject;
import hudson.model.ItemGroup;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginProjectActionTest {
    private ReportPluginProjectAction a;
    
    public ReportPluginProjectActionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {	
	a = new ReportPluginProjectAction(null);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getIconFileName method, of class ReportPluginProjectAction.
     */
    @Test
    public void testGetIconFileName() {
	assertEquals(a.getIconFileName(), Definitions.__ICON_FILE_NAME);
    }

    /**
     * Test of getDisplayName method, of class ReportPluginProjectAction.
     */
    @Test
    public void testGetDisplayName() {
	assertEquals(a.getDisplayName(), Definitions.__DISPLAY_NAME);
    }

    /**
     * Test of getUrlName method, of class ReportPluginProjectAction.
     */
    @Test
    public void testGetUrlName() {
	assertEquals(a.getUrlName(), Definitions.__URL_NAME);
    }

}
