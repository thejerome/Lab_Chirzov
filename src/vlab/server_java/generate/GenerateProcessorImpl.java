package vlab.server_java.generate;

import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.generate.tasks.ExploreTaskGenerator;
import vlab.server_java.generate.tasks.OscillationTaskGenerator;

/**
 * Simple GenerateProcessor implementation. Supposed to be changed as needed to
 * provide necessary Generate method support.
 */
public class GenerateProcessorImpl implements GenerateProcessor {


    @Override
    public GeneratingResult generate(String condition) {
        try {
            System.out.println("condition = " + condition);
            if (condition == null || condition.trim().isEmpty() || "explore".equals(condition.trim())) {
                return new ExploreTaskGenerator().generate(condition);
            } else if ("oscillation".equals(condition.trim())) {
                return new OscillationTaskGenerator().generate(condition);
            } else {
                return new GeneratingResult("Ошибка варианта", " ", " ");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new GeneratingResult("Ошибка варианта", " ", e.getLocalizedMessage());
        }
    }

    public interface TaskGenerator {
        GeneratingResult generate(String condition);
    }


}
