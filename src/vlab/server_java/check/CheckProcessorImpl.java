package vlab.server_java.check;

import rlcp.check.CheckingResult;
import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.CheckProcessor;
import rlcp.server.processor.check.PreCheckProcessor;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;
import vlab.server_java.check.tasks.ExploreTaskChecker;
import vlab.server_java.check.tasks.OscillationTaskChecker;
import vlab.server_java.generate.tasks.ExploreTaskGenerator;
import vlab.server_java.generate.tasks.OscillationTaskGenerator;
import vlab.server_java.model.util.Util;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static vlab.server_java.model.util.Util.prepareInputJsonString;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */
public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) throws Exception {
        //do check logic here

        try {

            condition = new ConditionForChecking(
                    condition.getId(),
                    condition.getTime(),
                    prepareInputJsonString(condition.getInput()),
                    prepareInputJsonString(condition.getOutput())
            );

            instructions = prepareInputJsonString(instructions);

            generatingResult = new GeneratingResult(
                    prepareInputJsonString(generatingResult.getText()),
                    prepareInputJsonString(generatingResult.getCode()),
                    prepareInputJsonString(generatingResult.getInstructions())
            );

            System.out.println("condition.getInput().trim() = " + condition.getInput().trim());

            if (condition.getInput() == null || condition.getInput().trim().isEmpty() || "explore".equals(condition.getInput().trim())) {
                return new ExploreTaskChecker().check(condition, instructions, generatingResult);
            } else if ("oscillation".equals(condition.getInput().trim())) {
                return new OscillationTaskChecker().check(condition, instructions, generatingResult);
            } else {
                return new CheckingSingleConditionResult(ZERO, "Ошибка варианта");
            }

        } catch (Exception e){
            e.printStackTrace();
            return new CheckingSingleConditionResult(ZERO, "Ошибка варианта");
        }

    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {}


    public interface TaskChecker {
        CheckingSingleConditionResult check(ConditionForChecking condition, String instructions, GeneratingResult generatingResult);
    }
}
