package vlab.server_java.generate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.model.PlotData;
import vlab.server_java.model.ToolState;
import vlab.server_java.model.Variant;
import vlab.server_java.model.tool.ToolModel;


import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static vlab.server_java.model.util.Util.bd;
import static vlab.server_java.model.util.Util.escapeParam;

/**
 * Simple GenerateProcessor implementation. Supposed to be changed as needed to
 * provide necessary Generate method support.
 */
public class GenerateProcessorImpl implements GenerateProcessor {



    Random random = new Random(System.nanoTime());

    @Override
    public GeneratingResult generate(String condition) {
        ObjectMapper mapper = new ObjectMapper();

        //do Generate logic here
        String text = "Ваш вариант загружен в установку";
        String code = " ";
        String instructions = " ";
        try {
            /*
            int radius_bounds_a = getRandomIntegerBetween(2, 6);
            int radius_bounds_b = radius_bounds_a + radius_bounds_a + 1;
            int mass = getRandomIntegerBetween(1, 5);

            double i = getRandomDoubleBetween(radius_bounds_a / 2, radius_bounds_a);
            double v = getRandomDoubleBetween(mass * 2 + 1, mass * 5);
*/


            BigDecimal light_slits_distance = bd("0.5");
            BigDecimal light_screen_distance = bd("0.7");
            BigDecimal[] light_screen_range = new BigDecimal[]{bd("0.01"), bd("2")};
            BigDecimal light_screen_step = bd("0.01");
            BigDecimal light_width = bd("0.05");
            BigDecimal[] light_width_range = new BigDecimal[]{bd("0.01"), bd("1")};
            BigDecimal light_width_step = bd("0.01");
            BigDecimal light_length = bd("721");
            BigDecimal[] light_length_range = new BigDecimal[]{bd("380"), bd("780")};
            BigDecimal light_length_step = bd("1");
            boolean right_slit_closed = false;
            boolean left_slit_closed = false;
            BigDecimal between_slits_width = bd("0.01");
            BigDecimal[] between_slits_range = new BigDecimal[]{bd("0.01"), bd("10")};
            BigDecimal between_slits_step = bd("0.01");
            PlotData plotData = ToolModel.buildPlot(
                    new ToolState(
                            light_slits_distance,
                            light_screen_distance,
                            light_width,
                            light_length,
                            between_slits_width,
                            left_slit_closed,
                            right_slit_closed
                    )
            );
            List<BigDecimal[]> data_plot_pattern = plotData.getData_plot();
            BigDecimal visibility = plotData.getVisibility();

            code = mapper.writeValueAsString(
                    new Variant(light_slits_distance,
                            light_screen_distance,
                            light_screen_range,
                            light_screen_step,
                            light_width,
                            light_width_range,
                            light_width_step,
                            light_length,
                            light_length_range,
                            light_length_step,
                            right_slit_closed,
                            left_slit_closed,
                            between_slits_width,
                            between_slits_range,
                            between_slits_step,
                            visibility,
                            data_plot_pattern
                    )
            );

            instructions = " ";
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
