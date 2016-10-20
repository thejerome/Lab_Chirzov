package vlab.server_java.check.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;
import vlab.server_java.check.CheckProcessorImpl;
import vlab.server_java.check.CheckProcessorImpl.TaskChecker;
import vlab.server_java.model.ToolState;
import vlab.server_java.model.Variant;
import vlab.server_java.model.tool.ToolModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
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

            BigDecimal eq0 = variant.getLight_width().multiply(variant.getBetween_slits_width()).divide(
                    variant.getLight_length().multiply((variant.getLight_slits_distance())),
                    10, ROUND_HALF_UP
            );
            BigDecimal eq1 = toolState.getLight_width().multiply(toolState.getBetween_slits_width()).divide(
                    toolState.getLight_length().multiply((toolState.getLight_slits_distance())),
                    10, ROUND_HALF_UP
            );

            System.out.println("variantMetric = " + eq0);
            System.out.println("userMetric = " + eq1);


            boolean isEqOk = eq0.subtract(eq1).setScale(6, ROUND_HALF_UP).compareTo(ZERO) == 0;

            List<BigDecimal[]> variantPlot = variant.getData_plot_pattern();
            List<BigDecimal[]> userPlot = ToolModel.buildPlot(toolState).getData_plot();

            BigDecimal zeroVariantValue = variantPlot.get(variantPlot.size() / 2)[1];
            BigDecimal zeroUserValue = userPlot.get(userPlot.size() / 2)[1];

            System.out.println("userPlot.get(userPlot.size() / 2)[0]; = " + userPlot.get(userPlot.size() / 2)[0]);

            System.out.println("zeroVariantValue = " + zeroVariantValue);
            System.out.println("zeroUserValue = " + zeroUserValue);

            boolean isZeroValuesOk = zeroUserValue.subtract(zeroVariantValue).abs().compareTo(bd(0.01)) <= 0;

            BigDecimal points;
            String comment;

            if(isLambdaOk && isAlphaOk && isSleetsOk){
                if (isEqOk){
                    points = ONE;
                    comment = "Верно!";
                } else {
                    points = bd(0.3);
                    comment = "Итоговая интерференционные не соотвествует требованиям задания.";
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
}
