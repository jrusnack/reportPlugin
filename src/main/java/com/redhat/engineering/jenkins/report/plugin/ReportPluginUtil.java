/*
 * Copyright (C) 2012 jrusnack
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.parser.Parser;
import com.redhat.engineering.jenkins.report.plugin.results.MatrixBuildTestResults;
import com.redhat.engineering.jenkins.report.plugin.results.MatrixRunTestResults;
import com.redhat.engineering.jenkins.report.plugin.results.TestResults;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.matrix.MatrixRun;
import hudson.model.Result;
import java.io.PrintStream;
import java.util.UUID;

/**
 *
 * @author jrusnack
 */
class ReportPluginUtil {

   protected static void initAllBuilds(MatrixProject project, ReportPluginTestAggregator testAggregator, String prefix, PrintStream logger) {
        for(MatrixBuild mbuild : project.getBuilds()){
            for(MatrixRun mrun : mbuild.getRuns()){
                gatherTests(mrun, testAggregator, prefix, mrun.getParent().getCombination().toString(), logger);
            }
        }
    }

    protected static boolean gatherTests(MatrixRun mrun, ReportPluginTestAggregator testAggregator, String prefix, String identifier, PrintStream logger) {
        
        TestResults rResults = new MatrixRunTestResults(UUID.randomUUID().toString());

        /*
         * Parse results
         */
        try {
            rResults = Parser.loadResults(mrun, Parser.getReportDir(mrun.getParentBuild()), 
                    logger, prefix, mrun.getParent().getCombination().toString());
        } catch (Throwable t) {
            /*
             * don't fail build if parser barfs, only print out the exception to
             * console.
             */
            if(logger != null) t.printStackTrace(logger);
        }

        if (rResults.getTestList().size() > 0) {
            /*
             * Set owner
             */
            rResults.setOwner(mrun);

            /*
             * Add matrix run rResults to parent build`s bResults
             */
            MatrixBuildTestResults bResults = testAggregator.getBuildResults(mrun.getParentBuild());
            bResults.addMatrixRunTestResults(mrun.toString(), mrun.getParent().getCombination(), rResults);
            rResults.setParent(bResults);

            if (rResults.getFailedTestCount() > 0) {
                mrun.setResult(Result.UNSTABLE);
            }

        } else {
            if(logger != null) logger.println("Found matching files but did not find any test results.");
            return true;
        }
        if(logger != null) {
            logger.println("[Report Plugin] Finished processing Matrix Run.");
            logger.println("[Report Plugin] Report Processing: FINISH");
        }

        return true;
    }
    
}
