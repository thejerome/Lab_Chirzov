package vlab.server_java.check.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;
import vlab.server_java.check.CheckProcessorImpl;
import vlab.server_java.check.CheckProcessorImpl.TaskChecker;
import vlab.server_java.model.PlotData;
import vlab.server_java.model.ToolState;
import vlab.server_java.model.Variant;
import vlab.server_java.model.tool.ToolModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.math.BigDecimal.*;
import static vlab.server_java.model.util.Util.bd;

/**
 * Created by efimchick on 19.10.16.
 */
public class OscillationTaskChecker implements TaskChecker{
    @Override
    public CheckProcessor.CheckingSingleConditionResult check(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ToolState toolState = objectMapper.readValue(instructions, ToolState.class);
            Variant variant = objectMapper.readValue(generatingResult.getCode(), Variant.class);
            String[] instructionValues = generatingResult.getInstructions().split(":");
            BigDecimal extraAlpha = new BigDecimal(instructionValues[0]);
            BigDecimal extraLambda = new BigDecimal(instructionValues[1]);

            boolean isLambdaOk = toolState.getLight_length().compareTo(variant.getLight_length().add(extraLambda)) == 0;
            boolean isAlphaOk = toolState.getLight_width().compareTo(variant.getLight_width().add(extraAlpha)) == 0;
            boolean isSleetsOk = !toolState.isLeft_slit_closed() && !toolState.isRight_slit_closed();


/* Chirzov unfinished version
            BigDecimal toSin0 = bd(2).multiply(new BigDecimal(PI))
                    .multiply(variant.getLight_width())
                    .multiply(variant.getBetween_slits_width())
                    .divide(
                            variant.getLight_length().multiply(variant.getLight_screen_distance().subtract(variant.getLight_slits_distance())),
                            ROUND_HALF_UP
            );

            BigDecimal toSin1 = bd(2).multiply(new BigDecimal(PI))
                    .multiply(toolState.getLight_width())
                    .multiply(toolState.getBetween_slits_width())
                    .divide(
                            toolState.getLight_length().multiply(toolState.getLight_screen_distance().subtract(toolState.getLight_slits_distance())),
                            ROUND_HALF_UP
            );

            BigDecimal oscilEq0 = bd(sin(toSin0.doubleValue())/toSin0.doubleValue());
            BigDecimal oscilEq1 = bd(sin(toSin1.doubleValue())/toSin1.doubleValue());

            System.out.println("variantOscilMetric = " + oscilEq0);
            System.out.println("userOscilMetric = " + oscilEq1);

            boolean isOscilEqOk =  oscilEq0.add(oscilEq1).setScale(6, ROUND_HALF_UP).compareTo(ZERO) == 0;


            BigDecimal periodEq0 = variant.getBetween_slits_width().divide(
                    variant.getLight_length().multiply((variant.getLight_slits_distance())),
                    10, ROUND_HALF_UP
            );
            BigDecimal periodEq1 = toolState.getBetween_slits_width().divide(
                    toolState.getLight_length().multiply((toolState.getLight_slits_distance())),
                    10, ROUND_HALF_UP
            );

            System.out.println("variantPeriodMetric = " + periodEq0);
            System.out.println("userPeriodMetric = " + periodEq1);

            boolean isPeriodEqOk = periodEq0.subtract(periodEq1).setScale(6, ROUND_HALF_UP).compareTo(ZERO) == 0;
            */
//----------Data based checking-------------------------------------------
            System.out.println("----------------------------------");

            boolean isVisibilityOk = false;
            boolean isZeroValuesOk = false;
            boolean isPeriodOk = false;

            PlotData userPlotData = ToolModel.buildPlot(toolState);

            {
                BigDecimal variantVisibility = variant.getVisibility();
                BigDecimal userVisibility = userPlotData.getVisibility();

                System.out.println();
                System.out.println("variantVisibility = " + variantVisibility);
                System.out.println("userVisibility = " + userVisibility);

                isVisibilityOk = variantVisibility.subtract(userVisibility).abs().compareTo(bd(0.01)) <= 0;

                System.out.println("isVisibilityOk = " + isVisibilityOk);
                System.out.println();
            }


            {

                List<BigDecimal[]> variantPlot = variant.getData_plot_pattern();
                List<BigDecimal[]> userPlot = userPlotData.getData_plot();

                BigDecimal zeroVariantValue = bd(2).subtract(variantPlot.get(variantPlot.size() / 2)[1]);
                BigDecimal zeroUserValue = bd(2).subtract(userPlot.get(userPlot.size() / 2)[1]);

                System.out.println("userPlot.get(userPlot.size() / 2)[0]; = " + userPlot.get(userPlot.size() / 2)[0]);

                System.out.println("zeroVariantValue = " + zeroVariantValue);
                System.out.println("zeroUserValue = " + zeroUserValue);

                isZeroValuesOk = zeroUserValue.add(zeroVariantValue).abs().compareTo(bd(0.02)) <= 0;

                System.out.println("isZeroValuesOk = " + isZeroValuesOk);
                System.out.println();

            }

            {
                BigDecimal variantPeriod = getPeriod(new ToolState(variant));
                BigDecimal userPeriod = getPeriod(toolState);

                System.out.println("variantPeriod = " + variantPeriod);
                System.out.println("userPeriod = " + userPeriod);

                isPeriodOk = variantPeriod.subtract(userPeriod).abs().compareTo(bd(0.03).multiply(variantPeriod)) <= 0;

                System.out.println("isPeriodOk = " + isPeriodOk);
            }
            System.out.println("----------------------------------");

//-----------------------------------------------------
            BigDecimal points;
            String comment;

            if(isLambdaOk && isAlphaOk && isSleetsOk){
                if (isVisibilityOk){
                    if (isPeriodOk){
                        if (isZeroValuesOk){
                            points = ONE;
                            comment = "Верно!";
                        } else {
                            points = bd(0.5);
                            comment = "Положение минимумов и максимумов не соответствуют требованиям задания.";
                        }
                    } else {
                        points = bd(0.4);
                        comment = "Расстояния между полосами не соответствуют исходным.";
                    }
                } else {
                    points = bd(0.3);
                    comment = "Видимость итоговой интерференционной картина не соотвествует исходной.";
                }
            } else {
                points = ZERO;
                comment = "Положение установки не соотвествует требованиям задания.";
            }


            return new CheckProcessor.CheckingSingleConditionResult(points, comment);



        } catch (IOException e) {
            e.printStackTrace();
            return new CheckProcessor.CheckingSingleConditionResult(ZERO, e.getMessage());
        }



    }

    private BigDecimal getPeriod(ToolState toolState) {
        return ToolModel.getPeriod(toolState.getBetween_slits_width(), toolState.getLight_length(), toolState.getLight_screen_distance().subtract(toolState.getLight_slits_distance()));
    }
}
