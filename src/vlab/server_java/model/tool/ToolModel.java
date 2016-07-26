package vlab.server_java.model.tool;

import vlab.server_java.model.PlotData;
import vlab.server_java.model.ToolState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static vlab.server_java.model.util.Util.bd;

/**
 * Created by efimchick on 04.07.16.
 */
public class ToolModel {

    public static final BigDecimal TEN_POW_MINUS_NINE = new BigDecimal("0.000000001");
    public static final BigDecimal TEN_POW_MINUS_THREE = new BigDecimal("0.001");
    public static final BigDecimal bdPI = new BigDecimal(PI);
    private static BigDecimal halfWidth = bd("0.03");
    private static BigDecimal xStep = bd("0.00025");
    private static final BigDecimal i0 = ONE;

    public static PlotData buildPlot(ToolState state){

        BigDecimal A = state.getBetween_slits_width().multiply(TEN_POW_MINUS_THREE);
        BigDecimal lambda = state.getLight_length().multiply(TEN_POW_MINUS_NINE);
        BigDecimal D = state.getLight_slits_distance();
        BigDecimal alpha = state.getLight_width().multiply(TEN_POW_MINUS_THREE);
        BigDecimal d = state.getLight_screen_distance().subtract(D);
        boolean leftSlitClosed = state.isLeft_slit_closed();
        boolean rightSlitClosed = state.isRight_slit_closed();

       PlotData plotData = null;

        if (bothSlitsAreOpen(leftSlitClosed, rightSlitClosed)){
            plotData = buildInterferentialPlotData(A, lambda, D, alpha, d);
        } else if (oneSlitIsOpen(leftSlitClosed, rightSlitClosed)){
            plotData = buildOneSlitBasedPlotData(alpha);
        } else if (noSlitIsOpen(leftSlitClosed, rightSlitClosed)) {
            plotData = buildEmptyPlotData();
        }


        return plotData;
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

    private static PlotData buildInterferentialPlotData(BigDecimal a, BigDecimal lambda, BigDecimal D, BigDecimal alpha, BigDecimal d) {
        int arrLength = bd(2).multiply(halfWidth).divide(xStep).intValue();
        List<BigDecimal[]> plotData = new ArrayList<BigDecimal[]>(arrLength);

        //PI * alpha * a / lambda * d;
        BigDecimal toSin = bdPI.multiply(alpha).multiply(a)
                .divide(lambda.multiply(D), HALF_UP);

        //|(sin(toSin) / toSin)|
        BigDecimal V = bd(sin(toSin.doubleValue())).divide(toSin, HALF_UP).abs();

        for (BigDecimal x = halfWidth.negate(); x.compareTo(halfWidth) <= 0; x = x.add(xStep)) {


            //(2 * PI * x * a) / (lambda * d2);
            BigDecimal toCos = bd(2).multiply(bdPI).multiply(x).multiply(a)
                    .divide(lambda.multiply(d), HALF_UP);
            //2 * i0 * alpha? * (1 + (sin(toSin) / toSin) * cos(toCos));
            BigDecimal i = bd(2).multiply(i0).multiply(
                    ONE.add(
                            bd(sin(toSin.doubleValue())).divide(toSin, HALF_UP)
                                    .multiply(bd(cos(toCos.doubleValue())))
                    )
            );

            BigDecimal[] row = new BigDecimal[2];
            row[0] = x;
            row[1] = i;

            plotData.add(row);
        }
        return new PlotData(plotData, V);
    }

    private static PlotData buildOneSlitBasedPlotData(BigDecimal alpha) {
        int arrLength = bd(2).multiply(halfWidth).divide(xStep).intValue();
        List<BigDecimal[]> plotData = new ArrayList<BigDecimal[]>(arrLength);
        for (BigDecimal x = halfWidth.negate(); x.compareTo(halfWidth) <= 0; x = x.add(xStep)) {
            BigDecimal i = i0.multiply(alpha);

            BigDecimal[] row = new BigDecimal[2];
            row[0] = x;
            row[1] = i;

            plotData.add(row);
        }
        return new PlotData(plotData, ZERO);
    }

    private static PlotData buildEmptyPlotData() {
        int arrLength = bd(2).multiply(halfWidth).divide(xStep).intValue();
        List<BigDecimal[]> plotData = new ArrayList<BigDecimal[]>(arrLength);
        for (BigDecimal x = halfWidth.negate(); x.compareTo(halfWidth) <= 0; x = x.add(xStep)) {
            BigDecimal i = ZERO;

            BigDecimal[] row = new BigDecimal[2];
            row[0] = x;
            row[1] = i;

            plotData.add(row);
        }
        return new PlotData(plotData, ZERO);
    }
}
