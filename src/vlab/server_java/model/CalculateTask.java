package vlab.server_java.model;

/**
 * Created by efimchick on 19.04.16.
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import vlab.server_java.model.util.HtmlParamEscaper;

import java.math.BigDecimal;
import java.util.Objects;

import static vlab.server_java.model.util.HtmlParamEscaper.shrink;

/**
 *
 Для calculate запроса - {
 "r": 3.5,
 "t": 3.6
 }


 */
public class CalculateTask {
    private final BigDecimal radius;
    private final BigDecimal time;

    @JsonCreator
    public CalculateTask(
            @JsonProperty("r") BigDecimal radius,
            @JsonProperty("t") BigDecimal time) {
        Objects.requireNonNull(radius);
        Objects.requireNonNull(time);
        this.radius = shrink(radius);
        this.time = shrink(time);

    }

    @JsonProperty("r")
    public BigDecimal getRadius() {
        return radius;
    }

    @JsonProperty("t")
    public BigDecimal getTime() {
        return time;
    }
}
