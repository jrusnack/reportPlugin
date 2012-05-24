/*
 * Copyright (C) 2012 Red Hat, Inc.     
 * 
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions of the 
 * GNU General Public License v.2.
 * 
 * Authors: Jan Rusnacko (jrusnack at redhat dot com)
 */



package com.redhat.engineering.jenkins.report.plugin;

import com.redhat.engineering.jenkins.report.plugin.results.Filter;
import com.redhat.engineering.jenkins.report.plugin.results.MatrixBuildTestResults;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.util.*;
import java.awt.Color;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.joda.time.LocalDate;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * FIXME: whole class
 * 
 * @author jrusnack
 */
public class ReportPluginPortlet extends DashboardPortlet {
        
        private int graphWidth = 300;
        private int graphHeight = 220;
        private int dateRange = 365;
        private String combinationFilter;

        @DataBoundConstructor
        public ReportPluginPortlet(String name, int graphWidth, int graphHeight, int dateRange, String combinationFilter) {
                super(name);
            this.graphWidth = graphWidth;
            this.graphHeight = graphHeight;
            this.dateRange = dateRange;
            this.combinationFilter = combinationFilter;
        }

        public int getDateRange() {
            return dateRange;
        }

        public int getGraphWidth() {
            return graphWidth <= 0 ? 300 : graphWidth;
        }

        public int getGraphHeight() {
            return graphHeight <= 0 ? 220 : graphHeight;
        }
        
        public String getCombinationFilter(){
            return combinationFilter;
        }

        /**
        * Graph of duration of tests over time.
        */
        public Graph getSummaryGraph() {
            // The standard equals doesn't work because two LocalDate objects can
            // be differente even if the date is the same (different internal timestamp)
            Comparator<LocalDate> localDateComparator = new Comparator<LocalDate>() {
                @Override public int compare(LocalDate d1, LocalDate d2) {
                    if(d1.isEqual(d2))
                    return 0;
                    if(d1.isAfter(d2))
                    return 1;
                    return -1;
                }
            };

                // We need a custom comparator for LocalDate objects
            final Map<LocalDate, TestResultAggrSummary> summaries = //new HashMap<LocalDate, TestResultSummary>();
                new TreeMap<LocalDate, TestResultAggrSummary>(localDateComparator);
            LocalDate today = new LocalDate();

            // for each job, for each day, add last build of the day to summary
            for (Job job : getDashboard().getJobs()) {
                    ReportPluginTestAggregator aggregator =  job.getAction(ReportPluginProjectAction.class).getTestAggregator();
                    Filter filter = job.getAction(ReportPluginProjectAction.class).getInitializedFilter();
                    filter.addCombinationFilter(combinationFilter);
                    Run firstBuild = aggregator.firstKey();
                    
                    if (firstBuild != null) { // execute only if job has builds
                        LocalDate runDay = new LocalDate(firstBuild.getTimestamp());
                        LocalDate firstDay = (dateRange != 0) ? new LocalDate().minusDays(dateRange) : runDay;

                        for (Run run : aggregator.keySet()){                            
                            runDay = new LocalDate(run.getTimestamp());
                            Run nextRun = run.getNextBuild();

                            if (nextRun != null) {
                                LocalDate nextRunDay = new LocalDate(nextRun.getTimestamp());
                                // skip run before firstDay, but keep if next build is after start date
                                if (!runDay.isBefore(firstDay)
                                    || runDay.isBefore(firstDay) && !nextRunDay.isBefore(firstDay)) {
                                    // if next run is not the same day, use this test to summarize
                                    if (nextRunDay.isAfter(runDay)) {
                                        summarize(summaries, aggregator.getBuildResults(run), filter, (runDay.isBefore(firstDay) ? firstDay : runDay), nextRunDay.minusDays(1));
                                    }
                                }
                            } else {
                                // use this run's test result from last run to today
                                summarize(summaries, aggregator.getBuildResults(run), filter, (runDay.isBefore(firstDay) ? firstDay : runDay), today);
                            }

                        }
                    }
            }

            return new Graph(-1, getGraphWidth(), getGraphHeight()) {

                        @Override
                        protected JFreeChart createGraph() {
                                final JFreeChart chart = ChartFactory.createStackedAreaChart(
                            null,                   // chart title
                            Definitions.__DASHBOARD_DATE,                   // category axis label
                            Definitions.__DASHBOARD_COUNT,                  // value axis label
                            buildDataSet(summaries), // data
                            PlotOrientation.VERTICAL, // orientation
                            false,                     // include legend
                            false,                     // tooltips
                            false                     // urls
                        );

                        chart.setBackgroundPaint(Color.white);

                        final CategoryPlot plot = chart.getCategoryPlot();

                        plot.setBackgroundPaint(Color.WHITE);
                        plot.setOutlinePaint(null);
                        plot.setForegroundAlpha(0.8f);
                        plot.setRangeGridlinesVisible(true);
                        plot.setRangeGridlinePaint(Color.black);

                        CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
                        plot.setDomainAxis(domainAxis);
                        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
                        domainAxis.setLowerMargin(0.0);
                        domainAxis.setUpperMargin(0.0);
                        domainAxis.setCategoryMargin(0.0);

                        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
                        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

                        StackedAreaRenderer ar = new StackedAreaRenderer2();
                        plot.setRenderer(ar);
                        ar.setSeriesPaint(0,ColorPalette.RED); // Failures.
                        ar.setSeriesPaint(1,ColorPalette.YELLOW); // Skips.
                        ar.setSeriesPaint(2,ColorPalette.BLUE); // Total.

                        // crop extra space around the graph
                        plot.setInsets(new RectangleInsets(0,0,0,5.0));

                                return chart;
                        }

                };
        }

        private CategoryDataset buildDataSet(Map<LocalDate, TestResultAggrSummary> summaries) {
            DataSetBuilder<String,LocalDateLabel> dsb = new DataSetBuilder<String,LocalDateLabel>();

            for (Map.Entry<LocalDate, TestResultAggrSummary> entry : summaries.entrySet()) {
                LocalDateLabel label = new LocalDateLabel(entry.getKey());
                //FIXME
                dsb.add( entry.getValue().getFailed(), Definitions.__DASHBOARD_FAILED, label);
                dsb.add( entry.getValue().getSkipped(), Definitions.__DASHBOARD_SKIPPED, label);
                dsb.add( entry.getValue().getPassed(), Definitions.__DASHBOARD_PASSED, label);
            }
            return dsb.build();
        }

        private void summarize(Map<LocalDate, TestResultAggrSummary> summaries,
            MatrixBuildTestResults results, Filter filter, LocalDate firstDay, LocalDate lastDay) {
            
            if(results != null){
                for (LocalDate curr = firstDay; curr.compareTo(lastDay) <= 0; curr = curr.plusDays(1)) {
                    TestResultAggrSummary trs = summaries.get(curr);
                    if (trs == null) {
                        trs = new TestResultAggrSummary();
                        summaries.put(curr, trs);
                    }
                    trs.addTestResult(results, filter);
                }
            }

        }

        @Extension
        public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

                @Override
                public String getDisplayName() {
                        return Definitions.__DASHBOARD_PORTLET;
                }
        }
        
}
