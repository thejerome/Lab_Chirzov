package vlab.server_java.model.tool;

import vlab.server_java.model.PlotData;
import vlab.server_java.model.ToolState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static vlab.server_java.model.util.Util.bd;

/**
 * Created by efimchick on 04.07.16.
 */
public class ToolModel {

    private static double halfWidth = 0.1;
    private static double xStep = 0.001;
    private static double i0 = 1;

    public static PlotData buildPlot(ToolState state){

        double A = state.getBetween_slits_width().doubleValue();
        double lambda = state.getLight_length().doubleValue() / pow(10, 9);
        double D = state.getLight_slits_distance().doubleValue();
        double alpha = state.getLight_width().doubleValue();
        double d = state.getLight_screen_distance().doubleValue();
        boolean leftSlitClosed = state.isLeft_slit_closed();
        boolean rightSlitClosed = state.isRight_slit_closed();

        List<BigDecimal[]> data_plot = null;

        if (bothSlitsAreOpen(leftSlitClosed, rightSlitClosed)){
            data_plot = buildInterferentialPlotData(A, lambda, D, alpha, d);
        } else if (oneSlitIsOpen(leftSlitClosed, rightSlitClosed)){
            data_plot = buildOneSlitBasedPlotData(alpha);
        } else if (noSlitIsOpen(leftSlitClosed, rightSlitClosed)){
            data_plot = buildEmptyPlotData();
        }

        for (BigDecimal[] row : data_plot) {
            row[2] = BigDecimal.ZERO;
            row[3] = BigDecimal.ZERO;
        }

        return new PlotData(data_plot);
    }

    private static boolean noSlitIsOpen(boolean leftSlitClosed, boolean rightSlitClosed) {
        return leftSlitClosed && rightSlitClosed;
    }

    private static boolean oneSlitIsOpen(boolean leftSlitClosed, boolean rightSlitClosed) {
        return (leftSlitClosed || rightSlitClosed) && !(leftSlitClosed && rightSlitClosed);
    }

    private static boolean bothSlitsAreOpen(boolean leftSlitClosed, boolean rightSlitClosed) {
        return !leftSlitClosed && !rightSlitClosed;
    }

    private static List<BigDecimal[]> buildInterferentialPlotData(double a, double lambda, double d, double alpha, double d2) {
        List<BigDecimal[]> plotData = new ArrayList<BigDecimal[]>((int)(2 * halfWidth / xStep));
        for (double x = -halfWidth; x <= halfWidth; x+=xStep) {

            double toSin = PI * alpha * a / lambda * d;
            double toCos = (2 * PI * x * a) / (lambda * d2);
            double i = 2 * i0 * alpha * (1 + (sin(toSin) / toSin) * cos(toCos));

            BigDecimal[] row = new BigDecimal[4];
            row[0] = bd(x);
            row[1] = bd(i);

            plotData.add(row);
        }
        return plotData;
    }

    private static List<BigDecimal[]> buildOneSlitBasedPlotData(double alpha) {
        List<BigDecimal[]> plotData = new ArrayList<BigDecimal[]>((int)(2 * halfWidth / xStep));
        for (double x = -halfWidth; x < halfWidth; x+=xStep) {
            double i = i0 * alpha;

            BigDecimal[] row = new BigDecimal[4];
            row[0] = bd(x);
            row[1] = bd(i);

            plotData.add(row);
        }
        return plotData;
    }

    private static List<BigDecimal[]> buildEmptyPlotData() {
        List<BigDecimal[]> plotData = new ArrayList<BigDecimal[]>((int)(2 * halfWidth / xStep));
        for (double x = -halfWidth; x < halfWidth; x+=xStep) {
            double i = 0;

            BigDecimal[] row = new BigDecimal[4];
            row[0] = bd(x);
            row[1] = bd(i);

            plotData.add(row);
        }
        return plotData;
    }
}
