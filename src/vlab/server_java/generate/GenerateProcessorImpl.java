package vlab.server_java.generate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.model.GenerateCodeResult;
import vlab.server_java.model.GenerateInstructionsResult;
import vlab.server_java.model.util.HtmlParamEscaper;

import java.math.BigDecimal;
import java.util.Random;

import static vlab.server_java.model.util.HtmlParamEscaper.escapeParam;

/**
 * Simple GenerateProcessor implementation. Supposed to be changed as needed to
 * provide necessary Generate method support.
 */
public class GenerateProcessorImpl implements GenerateProcessor {

    public static final BigDecimal ONE = new BigDecimal("1");
    public static final BigDecimal THREE = new BigDecimal("3");
    public static final BigDecimal SIX = new BigDecimal("3");
    public static final BigDecimal OTHREE = new BigDecimal("0.3");
    public static final BigDecimal OONE = new BigDecimal("0.1");

    Random random = new Random(System.nanoTime());

    @Override
    public GeneratingResult generate(String condition) {
        ObjectMapper mapper = new ObjectMapper();

        //do Generate logic here
        String text = "Ваш вариант загружен в установку";
        String code = null;
        String instructions = null;
        try {
            int radius_bounds_a = getRandomIntegerBetween(2, 6);
            int radius_bounds_b = radius_bounds_a + radius_bounds_a + 1;
            int mass = getRandomIntegerBetween(1, 5);

            double i = getRandomDoubleBetween(radius_bounds_a / 2, radius_bounds_a);
            double v = getRandomDoubleBetween(mass * 2 + 1, mass * 5);

            code = mapper.writeValueAsString(
                    new GenerateCodeResult(
                            new BigDecimal[]{
                                    new BigDecimal(radius_bounds_a),
                                    new BigDecimal((radius_bounds_b))
                            },
                            new BigDecimal(mass))
            );
            instructions = mapper.writeValueAsString(
                    new GenerateInstructionsResult(
                            new BigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_UP),
                            new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP)
                    )
            );
        } catch (JsonProcessingException e) {
            code = "Failed, " + e.getOriginalMessage();
        }

        return new GeneratingResult(text, escapeParam(code), escapeParam(instructions));
    }

    private int getRandomIntegerBetween(int a, int b) {
        return (a + random.nextInt(b - a + 1));
    }

    private double getRandomDoubleBetween(int a, int b) {
        return (a + random.nextDouble() * (b-a));
    }


}
