package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

import static vlab.server_java.model.util.HtmlParamEscaper.shrink;

/**
 * Created by efimchick on 19.04.16.
 * {
 "table":[
 {
 "r":3.4,
 "t1":3.345,
 "phi1":3.345,
 "t2":3.345,
 "phi2":3.345,
 "S":3.345
 },
 {
 "r":3.6,
 "t1":4.345,
 "phi1":4.345,
 "t2":4.345,
 "phi2":4.345,
 "S":4.345
 }
 ],
 "i":1.45,
 "v":1.45
 }
 */
public class CheckTask {

    private final List<Row> table;
    private final BigDecimal i;
    private final BigDecimal v;

    @JsonCreator
    public CheckTask(
            @JsonProperty("table") List<Row> table,
            @JsonProperty("i") BigDecimal i,
            @JsonProperty("v") BigDecimal v) {
        this.table = table;
        this.i = shrink(i);
        this.v = shrink(v);
    }

    public List<Row> getTable() {
        return table;
    }

    public BigDecimal getI() {
        return i;
    }

    public BigDecimal getV() {
        return v;
    }

    public static class Row{
        private final BigDecimal r;
        private final BigDecimal t1;
        private final BigDecimal phi1;
        private final BigDecimal t2;
        private final BigDecimal phi2;
        private final BigDecimal s;

        @JsonCreator
        public Row(
                @JsonProperty("r") BigDecimal r,
                @JsonProperty("t1") BigDecimal t1,
                @JsonProperty("phi1") BigDecimal phi1,
                @JsonProperty("t2") BigDecimal t2,
                @JsonProperty("phi2") BigDecimal phi2,
                @JsonProperty("S") BigDecimal s) {
            this.r = shrink(r);
            this.t1 = shrink(t1);
            this.phi1 = shrink(phi1);
            this.t2 = shrink(t2);
            this.phi2 = shrink(phi2);
            this.s = shrink(s);
        }

        public BigDecimal getR() {
            return r;
        }

        public BigDecimal getT1() {
            return t1;
        }

        public BigDecimal getPhi1() {
            return phi1;
        }

        public BigDecimal getT2() {
            return t2;
        }

        public BigDecimal getPhi2() {
            return phi2;
        }

        @JsonProperty("S")
        public BigDecimal getS() {
            return s;
        }
    }
}
