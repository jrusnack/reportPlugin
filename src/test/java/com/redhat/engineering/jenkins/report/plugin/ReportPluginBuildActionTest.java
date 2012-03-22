/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.engineering.jenkins.report.plugin;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public class ReportPluginBuildActionTest {
    private ReportPluginBuildAction a;
    
    public ReportPluginBuildActionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
	a = new ReportPluginBuildAction(null,null);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getIconFileName method, of class ReportPluginBuildAction.
     */
    @Test
    public void testGetIconFileName() {
	assertEquals(a.getIconFileName(), Definitions.__ICON_FILE_NAME);
    }

    /**
     * Test of getDisplayName method, of class ReportPluginBuildAction.
     */
    @Test
    public void testGetDisplayName() {
	assertEquals(a.getDisplayName(), Definitions.__DISPLAY_NAME);
    }

    /**
     * Test of getUrlName method, of class ReportPluginBuildAction.
     */
    @Test
    public void testGetUrlName() {
	assertEquals(a.getUrlName(), Definitions.__URL_NAME);
    }
}
