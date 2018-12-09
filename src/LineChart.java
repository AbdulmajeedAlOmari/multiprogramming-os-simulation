
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

class LineChart extends JFrame /*implements Runnable*/ {
    // Contains ram usage at every 1 OR 10 milliseconds
//    private LinkedList<Pair<Integer, Integer>> ramUsage;
    private XYSeries series;

    LineChart() {
//        ramUsage = new LinkedList<>();
        series = new XYSeries("RAM Usage");

        series.add(0, 0);

        init();
    }

    synchronized void addToDataset(int currentTime, int currentSize) {
        series.add(currentTime, currentSize);
    }

    void init() {
        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("RAM Usage");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(() -> {
            LineChart ex = this;
            ex.setVisible(true);
        });
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "RAM Usage per millisecond",
                "Time (ms)",
                "Size (MB)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("RAM Usage per millisecond",
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );

        return chart;

    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            LineChart ex = new LineChart();
//            ex.setVisible(true);
//        });
//    }

//	@Override
//	public void run() {
//
//		// TODO Auto-generated method stub
//
//	}
}