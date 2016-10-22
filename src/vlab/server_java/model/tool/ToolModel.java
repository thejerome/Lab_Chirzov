package vlab.server_java.model.tool;

import vlab.server_java.model.PlotData;
import vlab.server_java.model.ToolState;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_DOWN;
import static java.math.RoundingMode.HALF_UP;
import static vlab.server_java.model.util.Util.bd;

/**
 * Created by efimchick on 04.07.16.
 */
public class ToolModel {

    public static final BigDecimal TEN_POW_MINUS_NINE = new BigDecimal("0.000000001");
    public static final BigDecimal TEN_POW_MINUS_THREE = new BigDecimal("0.001");
    public static final BigDecimal bdPI = new BigDecimal(PI);
    private static final BigDecimal halfWidth = bd("0.03");
    private static final BigDecimal defaultXStep = bd("0.00025");
    private static final BigDecimal i0 = ONE;

    public static BigDecimal getPeriod(BigDecimal A, BigDecimal lambda, BigDecimal d) {
        return (d.multiply(lambda)).divide(A, HALF_UP);
    }

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
            plotData = buildOneSlitBasedPlotData();
        } else if (noSlitIsOpen(leftSlitClosed, rightSlitClosed)) {
            plotData = buildNoSlitsBasedPlotData();
        }


        return plotData;
    }

    private static PlotData buildOneSlitBasedPlotData() {
        return buildOneValuePlotData(i0);
    }
    private static PlotData buildNoSlitsBasedPlotData() {
        return buildOneValuePlotData(ZERO);
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

    private static PlotData buildInterferentialPlotData(BigDecimal A, BigDecimal lambda, BigDecimal D, BigDecimal alpha, BigDecimal d) {

        BigDecimal dataStep = defaultXStep;

        BigDecimal dataPeriod = getPeriod(A, lambda, d);
        BigDecimal xStepsPerPeriod = dataPeriod.divide(dataStep, HALF_UP);

        System.out.println("dataPeriod = " + dataPeriod);
        System.out.println("xStepsPerPeriod = " + xStepsPerPeriod);

        //handling small xStepsPerPeriod case
        if (xStepsPerPeriod.doubleValue() < 20){
            if (xStepsPerPeriod.doubleValue() >= 3){
                BigDecimal wholeXStepsPerPeriods = xStepsPerPeriod.setScale(0, HALF_UP);
                if (wholeXStepsPerPeriods.intValue() % 2 != 0){
                    wholeXStepsPerPeriods = wholeXStepsPerPeriods.add(ONE);
                }
                dataStep = dataPeriod.divide(wholeXStepsPerPeriods, HALF_UP);
            } else {
                return buildOneValuePlotData(i0.multiply(bd(2)));
            }
        }

        int arrLength = bd(2).multiply(halfWidth).divide(dataStep, HALF_UP).setScale(0, HALF_UP).intValue();
        List<BigDecimal[]> plotData = new LinkedList<BigDecimal[]>();

        //2 * PI * alpha * A / lambda * d;
        BigDecimal toSin = bd(2).multiply(bdPI).multiply(alpha).multiply(A)
                .divide(lambda.multiply(D), HALF_UP);

        //|(sin(toSin) / toSin)|
        BigDecimal V = bd(sin(toSin.doubleValue())).divide(toSin, HALF_UP).abs();

        for (BigDecimal x = dataStep; x.compareTo(halfWidth) <= 0; x = x.add(dataStep)) {

            //(4 * PI * x * A) / (lambda * d2);
            BigDecimal negX = x.negate();
            BigDecimal toCos = bd(4).multiply(bdPI).multiply(negX).multiply(A)
                    .divide(lambda.multiply(d), HALF_UP);
            //2 * i0 * alpha? * (1 + (sin(toSin) / toSin) * cos(toCos));
            BigDecimal i = bd(2).multiply(i0).multiply(
                    ONE.add(
                            bd(sin(toSin.doubleValue())).divide(toSin, HALF_UP)
                                    .multiply(bd(cos(toCos.doubleValue())))
                    )
            );

            BigDecimal[] row = new BigDecimal[2];
            row[0] = negX;
            row[1] = i;

            plotData.add(0, row);

        }

        for (BigDecimal x = ZERO; x.compareTo(halfWidth) <= 0; x = x.add(dataStep)) {


            //(4 * PI * x * A) / (lambda * d2);
            BigDecimal toCos = bd(4).multiply(bdPI).multiply(x).multiply(A)
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

        return new PlotData(new ArrayList<>(plotData), V);
    }



    private static PlotData buildOneValuePlotData(BigDecimal value) {
        int arrLength = bd(2).multiply(halfWidth).divide(defaultXStep).intValue();
        List<BigDecimal[]> plotData = new ArrayList<BigDecimal[]>(arrLength);
        for (BigDecimal x = halfWidth.negate(); x.compareTo(halfWidth) <= 0; x = x.add(defaultXStep)) {
            BigDecimal i = value;

            BigDecimal[] row = new BigDecimal[2];
            row[0] = x;
            row[1] = i;

            plotData.add(row);
        }
        return new PlotData(plotData, ZERO);
    }
}
