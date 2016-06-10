package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

import static vlab.server_java.model.util.HtmlParamEscaper.shrink;

/**
 * Created by efimchick on 26.04.16.
 */
public class GenerateInstructionsResult {
    private final BigDecimal i;
    private final BigDecimal v;

    @JsonCreator
    public GenerateInstructionsResult(
            @JsonProperty("i") BigDecimal i,
            @JsonProperty("v") BigDecimal v) {
        Objects.requireNonNull(i);
        Objects.requireNonNull(v);
        this.i = shrink(i);
        this.v = shrink(v);
    }

    public BigDecimal getI() {
        return i;
    }

    public BigDecimal getV() {
        return v;
    }
}
