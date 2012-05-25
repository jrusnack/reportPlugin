
package com.redhat.engineering.jenkins.report.plugin.util;

import com.redhat.engineering.jenkins.report.plugin.Definitions;
import com.redhat.engineering.jenkins.report.plugin.ReportPluginProjectAction;
import com.redhat.engineering.jenkins.report.plugin.ReportPluginTestAggregator;
import hudson.util.ChartUtil;
import hudson.util.ColorPalette;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.StackedAreaRenderer2;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.kohsuke.stapler.StaplerRequest;

/**
 * All credit due to authors of TestNG plugin
 */
public class GraphHelper {
     public static JFreeChart createChart( CategoryDataset dataset) {

      final JFreeChart chart = ChartFactory.createStackedAreaChart(
          null,                     // chart title
          null,                     // unused
          "Tests Count",            // range axis label
          dataset,                  // data
          PlotOrientation.VERTICAL, // orientation
          true,                     // include legend
          true,                     // tooltips
          false                     // urls
      );

      // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
      final LegendTitle legend = chart.getLegend();
      legend.setPosition(RectangleEdge.RIGHT);

      chart.setBackgroundPaint(Color.white);

      final CategoryPlot plot = chart.getCategoryPlot();
      plot.setBackgroundPaint(Color.WHITE);
      plot.setOutlinePaint(null);
      plot.setForegroundAlpha(0.8f);
      plot.setDomainGridlinesVisible(true);
      plot.setDomainGridlinePaint(Color.white);
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
      ar.setSeriesPaint(0, ColorPalette.RED); // Failures
      ar.setSeriesPaint(1, ColorPalette.BLUE); // Pass
      ar.setSeriesPaint(2, ColorPalette.YELLOW); // Skips

      // crop extra space around the graph
      plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

      return chart;
   }
}
