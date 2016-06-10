package vlab.server_java.calculate;

import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.calculate.CalculatingResult;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.calculate.CalculateProcessor;
import vlab.server_java.model.*;

import java.util.Arrays;

import static vlab.server_java.model.util.HtmlParamEscaper.escapeParam;
import static vlab.server_java.model.util.HtmlParamEscaper.prepareInputJsonString;

/**
 * Simple CalculateProcessor implementation. Supposed to be changed as needed to provide necessary Calculate method support.
 */
public class CalculateProcessorImpl implements CalculateProcessor {
    @Override
    public CalculatingResult calculate(String condition, String instructions, GeneratingResult generatingResult) {
        //do calculate logic here
        String text = "text";
        String code = "code";

        condition = prepareInputJsonString(condition);

        generatingResult = new GeneratingResult(
                generatingResult.getText(),
                prepareInputJsonString(generatingResult.getCode()),
                prepareInputJsonString(generatingResult.getInstructions())
        );

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            CalculateTask calculateTask = objectMapper.readValue(condition, CalculateTask.class);
            GenerateCodeResult varCode = objectMapper.readValue(generatingResult.getCode(), GenerateCodeResult.class);
            GenerateInstructionsResult varInstr = objectMapper.readValue(generatingResult.getInstructions(), GenerateInstructionsResult.class);
            CalculateCodeResult result = new RungeKuttaLab1().calculate(calculateTask, varCode, varInstr);
            return new CalculatingResult("ok", escapeParam(objectMapper.writeValueAsString(result)));
        } catch (Exception e) {
            return new CalculatingResult("error", e.toString());
        }
    }
}
