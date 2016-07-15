package vlab.server_java.model;

/**
 * Created by efimchick on 19.04.16.
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

import static vlab.server_java.model.util.Util.shrink;

/**
 *
 Для calculate запроса - {
 "light_slits_distance":50,
 "light_screen_distance":50,
 "light_width":50,
 "light_length":50,
 "left_slit_closed":false,
 "right_slit_closed":false,
 "between_slits_width":50}


 */
public class ToolState {
    private final BigDecimal light_slits_distance;
    private final BigDecimal light_screen_distance;
    private final BigDecimal light_width;
    private final BigDecimal light_length;
    private final BigDecimal between_slits_width;
    private final boolean left_slit_closed;
    private final boolean right_slit_closed;

    @JsonCreator
    public ToolState(
            @JsonProperty("light_slits_distance") BigDecimal light_slits_distance,
            @JsonProperty("light_screen_distance") BigDecimal light_screen_distance,
            @JsonProperty("light_width") BigDecimal light_width,
            @JsonProperty("light_length") BigDecimal light_length,
            @JsonProperty("between_slits_width") BigDecimal between_slits_width,
            @JsonProperty("left_slit_closed") boolean left_slit_closed,
            @JsonProperty("right_slit_closed") boolean right_slit_closed) {

        Objects.requireNonNull(light_slits_distance);
        Objects.requireNonNull(light_screen_distance);
        Objects.requireNonNull(light_length);
        Objects.requireNonNull(light_width);
        Objects.requireNonNull(between_slits_width);
        Objects.requireNonNull(left_slit_closed);
        Objects.requireNonNull(right_slit_closed);

        this.light_slits_distance = shrink(light_slits_distance);
        this.light_screen_distance = shrink(light_screen_distance);
        this.light_width = shrink(light_width);
        this.light_length = shrink(light_length);
        this.between_slits_width = shrink(between_slits_width);
        this.left_slit_closed = left_slit_closed;
        this.right_slit_closed = right_slit_closed;
    }

    public ToolState(Variant variant) {
        this(
                variant.getLight_slits_distance(),
                variant.getLight_screen_distance(),
                variant.getLight_width(),
                variant.getLight_length(),
                variant.getBetween_slits_width(),
                variant.isLeft_slit_closed(),
                variant.isRight_slit_closed()
        );
    }

    public BigDecimal getLight_slits_distance() {
        return light_slits_distance;
    }

    public BigDecimal getLight_screen_distance() {
        return light_screen_distance;
    }

    public BigDecimal getLight_width() {
        return light_width;
    }

    public BigDecimal getLight_length() {
        return light_length;
    }

    public BigDecimal getBetween_slits_width() {
        return between_slits_width;
    }

    public boolean isLeft_slit_closed() {
        return left_slit_closed;
    }

    public boolean isRight_slit_closed() {
        return right_slit_closed;
    }
}
