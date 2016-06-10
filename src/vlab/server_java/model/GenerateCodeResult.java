package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

import static vlab.server_java.model.util.HtmlParamEscaper.shrink;

/**
 * Created by efimchick on 19.04.16.
 */
public class GenerateCodeResult {

    private final BigDecimal[] radiusBounds;
    private final BigDecimal mass;

    @JsonCreator
    public GenerateCodeResult(
            @JsonProperty("radius_bounds") BigDecimal[] radius_bounds,
            @JsonProperty("mass") BigDecimal mass) {
        Objects.requireNonNull(radius_bounds);
        Objects.requireNonNull(mass);
        if (radius_bounds.length != 2){
            throw new IllegalArgumentException("Radius bounds should have 2 elements but there was " + radius_bounds.length);
        }

        this.radiusBounds = radius_bounds;
        this.mass = shrink(mass);
    }

    @JsonProperty("radius_bounds")
    public BigDecimal[] getRadiusBounds() {
        return radiusBounds;
    }

    public BigDecimal getMass() {
        return mass;
    }
}
