package vlab.server_java.calculate;

import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.calculate.CalculatingResult;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.calculate.CalculateProcessor;
import vlab.server_java.model.*;
import vlab.server_java.model.tool.ToolModel;

import static vlab.server_java.model.util.Util.escapeParam;
import static vlab.server_java.model.util.Util.prepareInputJsonString;

/**
 * Simple CalculateProcessor implementation. Supposed to be changed as needed to provide necessary Calculate method support.
 */
public class CalculateProcessorImpl implements CalculateProcessor {
    @Override
    public CalculatingResult calculate(String condition, String instructions, GeneratingResult generatingResult) {
        //do calculate logic here
        String text = "text";
        String code = "code";

        instructions = prepareInputJsonString(instructions);

        generatingResult = new GeneratingResult(
                generatingResult.getText(),
                prepareInputJsonString(generatingResult.getCode()),
                prepareInputJsonString(generatingResult.getInstructions())
        );

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ToolState toolState = objectMapper.readValue(instructions, ToolState.class);
            Variant varCode = objectMapper.readValue(generatingResult.getCode(), Variant.class);

            return new CalculatingResult("ok", escapeParam(escapeParam(objectMapper.writeValueAsString(ToolModel.buildPlot(toolState)))));
        } catch (Exception e) {
            e.printStackTrace();
            return new CalculatingResult("error", e.getMessage());
        }
    }
}
