package parallelImage.histogram;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author : Enrico Gamil Toros Project name : Parallel-Image-processing
 * @version : 1.0
 * @since : 19.11.22
 **/
public class Histogram {

    private final int[] redBucket;

    private final int[] greenBucket;

    private final int[] blueBucket;

    private XYChart chart = null;

    public Histogram(int[] redBucket, int[] greenBucket, int[] blueBucket) {
        this.redBucket = redBucket;
        this.greenBucket = greenBucket;
        this.blueBucket = blueBucket;
    }

    /**
     * Saves Histogram as Image.
     *
     * @param outputImage file where to save Image
     * @throws IOException if an I/O error occurs.
     */
    public void saveHistogram(File outputImage) throws IOException {
        if (outputImage.isDirectory()) {
            throw new IllegalArgumentException("outputImage has to be a File.");
        }
        if (chart == null) {
            plotHistogram();
        }
        chart.getStyler().setToolTipsEnabled(false);
        chart.getStyler().setZoomEnabled(false);
        BitmapEncoder.saveJPGWithQuality(chart, outputImage.getAbsolutePath(), 1); // 1 = high quality
    }

    /**
     * Show the Histogram on an Interface
     */
    public void showHistogram() {
        if (chart == null) {
            plotHistogram();
        }
        new SwingWrapper(chart).displayChart();
    }

    private void plotHistogram() {
        // Create Chart
        this.chart = new XYChartBuilder().width(1600).height(900).title("Histogram").xAxisTitle("Value").yAxisTitle("Count").build();
        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
        chart.getStyler().setPlotGridLinesVisible(false);
        chart.getStyler().setZoomEnabled(true);
        chart.getStyler().setAntiAlias(true);
        chart.getStyler().setToolTipsEnabled(true);
        // Series
        int[] value = new int[256];
        for (int i = 0; i < 256; i++) {
            value[i] = i;
        }
        // Red
        XYSeries red = chart.addSeries("Red", value, getRedBucket());
        red.setFillColor(new Color(255, 0, 0, 100));
        red.setMarker(SeriesMarkers.NONE);
        red.setLineColor(Color.RED);
        red.setSmooth(true);
        // Green
        XYSeries green = chart.addSeries("Green", value, getGreenBucket());
        green.setFillColor(new Color(0, 255, 0, 100));
        green.setMarker(SeriesMarkers.NONE);
        green.setLineColor(Color.GREEN);
        green.setSmooth(true);
        // Blue
        XYSeries blue = chart.addSeries("Blue", value, getBlueBucket());
        blue.setFillColor(new Color(0, 0, 255, 100));
        blue.setMarker(SeriesMarkers.NONE);
        blue.setLineColor(Color.BLUE);
        blue.setSmooth(true);
    }

    public int[] getRedBucket() {
        return redBucket;
    }

    public int[] getGreenBucket() {
        return greenBucket;
    }

    public int[] getBlueBucket() {
        return blueBucket;
    }
}
