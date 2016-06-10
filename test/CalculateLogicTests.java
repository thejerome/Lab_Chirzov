import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import vlab.server_java.calculate.CalculateProcessorImpl;
import vlab.server_java.model.*;
import vlab.server_java.model.util.HtmlParamEscaper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static vlab.server_java.model.util.HtmlParamEscaper.prepareInputJsonString;

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
    public void testJson() {
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        CalculateTask task = new CalculateTask(new BigDecimal("3"), new BigDecimal("6.546"));
        CalculateCodeResult result = new CalculateCodeResult(new ArrayList<BigDecimal[]>(){
            {
                add(new BigDecimal[]{new BigDecimal("0.01"), new BigDecimal("0.02"), new BigDecimal("0.03")});
                add(new BigDecimal[]{new BigDecimal("0.02"), new BigDecimal("0.04"), new BigDecimal("0.06")});
                add(new BigDecimal[]{new BigDecimal("0.03"), new BigDecimal("0.06"), new BigDecimal("0.09")});
            }
        });
        try {
            System.out.println(objectMapper.writeValueAsString(
                            objectMapper.readValue(
                                    objectMapper.writeValueAsString(task), CalculateTask.class)
                            )
            );
            System.out.println(objectMapper.writeValueAsString(
                            objectMapper.readValue(
                                    objectMapper.writeValueAsString(result), CalculateCodeResult.class)
                    )
            );

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRK() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        CalculateTask task = new CalculateTask(new BigDecimal("1"), new BigDecimal("6.546"));
        GenerateCodeResult variantCode = new GenerateCodeResult(new BigDecimal[]{new BigDecimal(3), new BigDecimal(6)}, ONE);
        GenerateInstructionsResult variantInstr = new GenerateInstructionsResult(new BigDecimal(0.3), new BigDecimal(0.1));

        CalculateCodeResult result = new RungeKuttaLab1().calculate(task, variantCode, variantInstr);

        System.out.println(objectMapper.writeValueAsString(result));
    }

    @Test
    public void realLifeParsingCalculateTask() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String value = prepareInputJsonString("{\"r\":3,\"t\":8}");
        CalculateTask t = objectMapper.readValue(value, CalculateTask.class);

        assertNotNull(t);
        assertEquals(3, t.getRadius().intValue());
    }


}
