import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.factory.ProcessorFactory;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.model.CalculateTask;
import vlab.server_java.model.CheckTask;
import vlab.server_java.model.CheckTask.Row;
import vlab.server_java.model.GenerateCodeResult;
import vlab.server_java.model.GenerateInstructionsResult;
import vlab.server_java.model.util.HtmlParamEscaper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static vlab.server_java.model.util.HtmlParamEscaper.*;
import static vlab.server_java.model.util.HtmlParamEscaper.prepareInputJsonString;

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
    public void testJson() {
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        GenerateCodeResult variant = new GenerateCodeResult(new BigDecimal[]{new BigDecimal(3), new BigDecimal(6)}, ONE);
        CheckTask solution = new CheckTask(
                new ArrayList<Row>(){
                    {
                        add(new Row(new BigDecimal("1"), new BigDecimal("0.02"), new BigDecimal("0.03"), new BigDecimal("0.02"), new BigDecimal("0.03"), new BigDecimal("0.02")));
                        add(new Row(new BigDecimal("1"), new BigDecimal("0.02"), new BigDecimal("0.03"), new BigDecimal("0.02"), new BigDecimal("0.03"), new BigDecimal("0.02")));
                        add(new Row(new BigDecimal("1"), new BigDecimal("0.02"), new BigDecimal("0.03"), new BigDecimal("0.02"), new BigDecimal("0.03"), new BigDecimal("0.02")));
                    }
                }, new BigDecimal("0.654"), new BigDecimal("654163")
        );

        try {
            System.out.println(objectMapper.writeValueAsString(
                            objectMapper.readValue(
                                    objectMapper.writeValueAsString(variant), GenerateCodeResult.class)
                    )
            );
            System.out.println(objectMapper.writeValueAsString(
                            objectMapper.readValue(
                                    objectMapper.writeValueAsString(solution), CheckTask.class)
                    )
            );
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void realLifeParsingGeneratingResult() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String code = prepareInputJsonString("{&quot;radius_bounds&quot;:[3,3],&quot;mass&quot;:1}");
        String inst = prepareInputJsonString("{&quot;i&quot;:0.3,&quot;v&quot;:0.1}");
        GenerateCodeResult c = objectMapper.readValue(code, GenerateCodeResult.class);
        GenerateInstructionsResult i = objectMapper.readValue(inst, GenerateInstructionsResult.class);

        assertNotNull(c);
        assertNotNull(i);
    }


}
