package vlab.server_java.generate.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.generate.GeneratingResult;
import vlab.server_java.generate.GenerateProcessorImpl;
import vlab.server_java.model.PlotData;
import vlab.server_java.model.ToolState;
import vlab.server_java.model.Variant;
import vlab.server_java.model.tool.ToolModel;

import java.math.BigDecimal;
import java.util.List;

import static vlab.server_java.model.util.Util.*;
import static vlab.server_java.model.util.Util.getRandomBoolean;

/**
 * Created by efimchick on 17.10.16.
 */
public class OscillationTaskGenerator implements GenerateProcessorImpl.TaskGenerator {
    @Override
    public GeneratingResult generate(String condition) {
        ObjectMapper mapper = new ObjectMapper();

        //do Generate logic here
        String text = "Ваш вариант загружен в установку";
        String code = " ";
        String instructions = " ";
        try {

            BigDecimal light_slits_distance = bd("0.4");
            BigDecimal light_screen_distance = bd("1.5");
            BigDecimal[] light_screen_range = new BigDecimal[]{bd("0.01"), bd("2")};
            BigDecimal light_screen_step = bd("0.01");
            BigDecimal light_width = bd("0.9");
            BigDecimal[] light_width_range = new BigDecimal[]{bd("0.01"), bd("10")};
            BigDecimal light_width_step = bd("0.01");
            BigDecimal light_length = bd("600");
            BigDecimal[] light_length_range = new BigDecimal[]{bd("380"), bd("780")};
            BigDecimal light_length_step = bd("1");
            boolean right_slit_closed = false;
            boolean left_slit_closed = false;
            BigDecimal between_slits_width = bd("0.2");
            BigDecimal[] between_slits_range = new BigDecimal[]{bd("0.01"), bd("3")};
            BigDecimal between_slits_step = bd("0.01");


            BigDecimal extra_alpha = bd(getRandomDoubleBetween(1, 3)/10).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal extra_lambda = bd(getRandomIntegerBetween(20, 100));

            if (getRandomBoolean()){
                extra_alpha = extra_alpha.negate();
            } else {
                extra_lambda = extra_lambda.negate();
            }

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

            text = "Требуется изменить значение длины света на " + extra_lambda + " нм, а ширину светящейся полоски - на " + extra_alpha +" мм.";


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

            instructions = extra_alpha.toString() + ":" + extra_lambda.toString();
        } catch (JsonProcessingException e) {
            code = "Failed, " + e.getOriginalMessage();
        }

        return new GeneratingResult(text, escapeParam(code), escapeParam(instructions));
    }
}
