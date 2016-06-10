package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

import static vlab.server_java.model.util.HtmlParamEscaper.shrink;

/**
 * Created by efimchick on 26.04.16.
 */
public class GenerateTask {
    private final BigDecimal radius;
    private final BigDecimal time;

    @JsonCreator
    public GenerateTask(
            @JsonProperty("radius") BigDecimal radius,
            @JsonProperty("time") BigDecimal time) {
        Objects.requireNonNull(radius);
        Objects.requireNonNull(time);
        this.radius = shrink(radius);
        this.time = shrink(time);
    }

    public BigDecimal getRadius() {
        return radius;
    }

    public BigDecimal getTime() {
        return time;
    }
}
