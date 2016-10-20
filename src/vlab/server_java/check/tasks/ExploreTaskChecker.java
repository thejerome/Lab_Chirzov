package vlab.server_java.check.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.calculate.CalculatingResult;
import rlcp.check.CheckingResult;
import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;
import rlcp.server.processor.check.CheckProcessor.CheckingSingleConditionResult;
import vlab.server_java.check.CheckProcessorImpl;
import vlab.server_java.model.ToolState;
import vlab.server_java.model.Variant;

import java.io.IOException;
import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static vlab.server_java.model.util.Util.bd;

/**
 * Created by efimchick on 19.10.16.
 */
public class ExploreTaskChecker implements CheckProcessorImpl.TaskChecker {
    @Override
    public CheckingSingleConditionResult check(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ToolState toolState = objectMapper.readValue(instructions, ToolState.class);
            Variant variant = objectMapper.readValue(generatingResult.getCode(), Variant.class);
            BigDecimal extraLambda = new BigDecimal(generatingResult.getInstructions());

            boolean isLambdaOk = toolState.getLight_length().compareTo(variant.getLight_length().add(extraLambda)) == 0;
            boolean isDOk = toolState.getLight_slits_distance().compareTo(variant.getLight_slits_distance()) == 0;
            boolean isAlphaOk = toolState.getLight_width().compareTo(variant.getLight_width()) == 0;
            boolean isAOk = toolState.getBetween_slits_width().compareTo(variant.getBetween_slits_width().multiply(bd(3))) == 0;
            boolean isSleetsOk = !toolState.isLeft_slit_closed() && !toolState.isRight_slit_closed();

            BigDecimal eq0 = variant.getBetween_slits_width().divide(
                    variant.getLight_length().multiply(variant.getLight_screen_distance().subtract(variant.getLight_slits_distance())),
                    10, ROUND_HALF_UP
            );
            BigDecimal eq1 = toolState.getBetween_slits_width().divide(
                    toolState.getLight_length().multiply(toolState.getLight_screen_distance().subtract(toolState.getLight_slits_distance())),
                    10, ROUND_HALF_UP
            );

            System.out.println("variantMetric = " + eq0);
            System.out.println("userMetric = " + eq1);

            boolean isEqOk = eq0.subtract(eq1).setScale(6, ROUND_HALF_UP).compareTo(ZERO) == 0;


            BigDecimal points;
            String comment;

            if(isLambdaOk && isDOk && isAlphaOk && isAOk && isSleetsOk){
                if (isEqOk){
                    points = ONE;
                    comment = "Верно!";
                } else {
                    points = bd(0.3);
                    comment = "Исходная и итоговая интерференционные картины не совпадают";
                }
            } else {
                points = ZERO;
                comment = "Положение установки не соотвествует требованиям задания.";
            }


            return new CheckingSingleConditionResult(points, comment);



        } catch (IOException e) {
            e.printStackTrace();
            return new CheckingSingleConditionResult(ZERO, e.getMessage());
        }




    }
}
