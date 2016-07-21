import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.Processor;
import rlcp.server.processor.factory.ProcessorFactory;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.generate.GenerateProcessorImpl;
import vlab.server_java.model.Variant;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-*-server-config.xml")
@ActiveProfiles(profiles = "java")
//@ActiveProfiles(profiles = "js")
public class GenerateLogicTests {

    @Autowired
    private ProcessorFactory generateProcessor;

    @Test
    public void testProcess() {
        GenerateProcessor processor = (GenerateProcessor) generateProcessor.getInstance();
        GeneratingResult result = processor.generate("generate");
        assertThat(result.getText(), is(not(equalTo(""))));
        assertThat(result.getCode(), is(not(equalTo(""))));
        assertThat(result.getInstructions(), is(not(equalTo(""))));
    }

    @Test
    public void testJson() throws Exception{
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object

            System.out.println(objectMapper.writeValueAsString(
                            objectMapper.readValue(
                                    "{\"light_slits_distance\": 1,\n" +
                                            " \"light_screen_distance\": 40,\n" +
                                            " \"light_screen_range\": [1, 40],\n" +
                                            " \"light_screen_step\": 1,\n" +
                                            " \"light_width\": 5,\n" +
                                            " \"light_width_range\": [0.1, 10],\n" +
                                            " \"light_width_step\": 0.1,\n" +
                                            " \"light_length\": 721,\n" +
                                            " \"light_length_range\": [380, 780],\n" +
                                            " \"light_length_step\": 1,\n" +
                                            " \"right_slit_closed\": false,\n" +
                                            " \"left_slit_closed\": false,\n" +
                                            " \"between_slits_width\": 1,\n" +
                                            " \"between_slits_range\": [0.1, 2],\n" +
                                            " \"between_slits_step\": 0.1,\n" +
                                            " \"visibility\": 1,\n" +
                                            " \"data_plot_pattern\": [\n" +
                                            " [0.01, 0.3, 3.5, 1],\n" +
                                            " [1.01, 0.8, 3.5, 2],\n" +
                                            " [2.02, 1.2, 3.6, 0],\n" +
                                            " [3.03, 1.4, 3.7, 3],\n" +
                                            " [4.04, 1.9, 3.1, 0],\n" +
                                            " [5.05, 2.3, 2.5, 5],\n" +
                                            " [6.06, 2.8, 4, 1],\n" +
                                            " [7.00, 2.9, 1, 3],\n" +
                                            " [10.5, 3.2, 1, 2]\n" +
                                            " ]" +
                                            "}", Variant.class)
                    )
            );



    }

    @Test
    public void testGenerateProcessorProcesses() throws Exception{

        GenerateProcessor generateProcessor = new GenerateProcessorImpl();

        GeneratingResult generatingResult = generateProcessor.generate("no matter");

        System.out.println(generatingResult.getCode());


    }




}
