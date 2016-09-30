import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rlcp.calculate.CalculatingResult;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.calculate.CalculateProcessor;
import rlcp.server.processor.factory.ProcessorFactory;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.calculate.CalculateProcessorImpl;
import vlab.server_java.generate.GenerateProcessorImpl;
import vlab.server_java.model.*;
import vlab.server_java.model.util.Util;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static vlab.server_java.model.util.Util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-*-server-config.xml")
@ActiveProfiles(profiles = "java")
//@ActiveProfiles(profiles = "js")
public class CalculateLogicTests {

    @Autowired
    private ProcessorFactory calculateProcessor;

    @Test
    public void testProcess() {
        CalculateProcessor processor = (CalculateProcessor) calculateProcessor.getInstance();

        GeneratingResult generatingResult = mock(GeneratingResult.class);
        when(generatingResult.getText()).thenReturn("textPreGenerated");
        when(generatingResult.getCode()).thenReturn("codePreGenerated");
        when(generatingResult.getInstructions()).thenReturn("instructionsPreGenerated");

        CalculatingResult calculatingResult = processor.calculate("condition", "instructions", generatingResult);
        assertThat(calculatingResult.getText(), is(not(equalTo(""))));
        assertThat(calculatingResult.getCode(), is(not(equalTo(""))));
    }


    @Test
    public void testJson() throws  Exception{
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object


            System.out.println(objectMapper.writeValueAsString(
                            objectMapper.readValue(
                                    "{\n" +
                                            " \"light_slits_distance\":50,\n" +
                                            " \"light_screen_distance\":50,\n" +
                                            " \"light_width\":50,\n" +
                                            " \"light_length\":50,\n" +
                                            " \"left_slit_closed\":false,\n" +
                                            " \"right_slit_closed\":false,\n" +
                                            " \"between_slits_width\":50}",
                                    ToolState.class)
                            )
            );
            System.out.println(objectMapper.writeValueAsString(
                            objectMapper.readValue(
                                    "{ \"visibility\": 1, " +
                                            "\"data_plot\": [\n" +
                                            " [0.01, 0, 3.5, 2],\n" +
                                            " [1.01, 0.8, 3.5, 2],\n" +
                                            " [2.02, 3, 6, 0],\n" +
                                            " [3.03, 1.4, 0, 5],\n" +
                                            " [4.04, 0.9, 3.1, 0],\n" +
                                            " [5.05, 2.3, 2.5, 0],\n" +
                                            " [6.06, 2.8, 8, 1],\n" +
                                            " [7.00, 2, 1, 5],\n" +
                                            " [10.5, 3.2, 1, 2]\n" +
                                            " ]}", PlotData.class)
                    )
            );

    }

    @Test
    public void testRealWorldJson() throws  Exception{
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object


            System.out.println(objectMapper.writeValueAsString(
                            objectMapper.readValue(
                                    "{\"light_slits_distance\":0.01,\"light_screen_distance\":0.4,\"light_width\":0.05,\"light_length\":721,\"left_slit_closed\":false,\"right_slit_closed\":false,\"between_slits_width\":0.01}",
                                    ToolState.class)
                            )
            );



    }

    @Test
    public void testRealWorldCalculateProcesses() throws  Exception{
        GenerateProcessor generateProcessor = new GenerateProcessorImpl();
        GeneratingResult generatingResult = generateProcessor.generate("no matter");

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        ToolState toolState = objectMapper.readValue(
                "{\"light_slits_distance\":0.01,\"light_screen_distance\":0.4,\"light_width\":0.05,\"light_length\":721,\"left_slit_closed\":false,\"right_slit_closed\":false,\"between_slits_width\":0.01}",
                ToolState.class);


        CalculateProcessor calculateProcessor = new CalculateProcessorImpl();

        CalculatingResult calculatingResult = calculateProcessor.calculate("", escapeParam(escapeParam(objectMapper.writeValueAsString(toolState))), generatingResult);

        System.out.println(calculatingResult.getCode());
    }

    @Test
    public void testSmallPeriodDataRealWorldCalculateProcesses() throws  Exception{
        GenerateProcessor generateProcessor = new GenerateProcessorImpl();
        GeneratingResult generatingResult = generateProcessor.generate("no matter");

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        ToolState toolState = objectMapper.readValue(
                "{\"light_slits_distance\":0.5,\"light_screen_distance\":1.5,\"light_width\":0.05,\"light_length\":500,\"left_slit_closed\":false,\"right_slit_closed\":false,\"between_slits_width\":0.09}",
                ToolState.class);


        CalculateProcessor calculateProcessor = new CalculateProcessorImpl();

        CalculatingResult calculatingResult = calculateProcessor.calculate("", escapeParam(objectMapper.writeValueAsString(toolState)), generatingResult);

        System.out.println(calculatingResult.getCode());

        assertNotNull(toolState);
        assertNotNull(calculatingResult);
    }


@Test
    public void testCalculateProcesses() throws  Exception{
        GenerateProcessor generateProcessor = new GenerateProcessorImpl();
        GeneratingResult generatingResult = generateProcessor.generate("no matter");

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        ToolState toolState = new ToolState(
                objectMapper.readValue(
                        prepareInputJsonString(generatingResult.getCode()),
                        Variant.class
                )
        );

        CalculateProcessor calculateProcessor = new CalculateProcessorImpl();

        CalculatingResult calculatingResult = calculateProcessor.calculate("", escapeParam(objectMapper.writeValueAsString(toolState)), generatingResult);

        System.out.println(calculatingResult.getCode());
    }


}
