package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import vlab.server_java.model.util.HtmlParamEscaper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static vlab.server_java.model.util.HtmlParamEscaper.shrink;

/**
 * Created by efimchick on 19.04.16.
 *  результаты calculate^
 {"table":[
 [0.01, 3.3, 3.5],
 [0.02, 3.3, 3.5],
 [0.03, 3.3, 3.5],
 [0.04, 3.3, 3.5],
 [0.05, 3.3, 3.5],
 [0.06, 3.3, 3.5],
 [0.07, 3.3, 3.5]
 ]}
 */
public class CalculateCodeResult {

    private final List<Row> table;

    @JsonCreator
    public CalculateCodeResult(@JsonProperty("table") List<BigDecimal[]> table) {
        this.table = table.stream().map(Row::new).collect(Collectors.toList());
    }

    @JsonProperty("table")
    public List<BigDecimal[]> getTable() {
        return table.stream().map(Row::getRow).collect(Collectors.toList());
    }

    public static class Row{
        private final BigDecimal t;
        private final BigDecimal phi;
        private final BigDecimal omega;

        @JsonCreator
        public Row(BigDecimal[] values) {
            this.t = shrink(values[0]);
            this.phi = shrink(values[1]);
            this.omega = shrink(values[2]);
        }

        public BigDecimal[] getRow() {
            return new BigDecimal[]{t, phi, omega};
        }


    }
}
