package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static vlab.server_java.model.util.Util.shrink;

/**
 * Created by efimchick on 19.04.16.
 *
 * "light_slits_distance": 1,
 "light_screen_distance": 40,
 "light_screen_range": [1, 40],
 "light_screen_step": 1,
 "light_width": 5,
 "light_width_range": [0.1, 10],
 "light_width_step": 0.1,
 "light_length": 721,
 "light_length_range": [380, 780],
 "light_length_step": 1,
 "right_slit_closed": false,
 "left_slit_closed": false,
 "between_slits_width": 1,
 "between_slits_range": [0.1, 2],
 "between_slits_step": 0.1,
 "data_plot_pattern": [
 [0.01, 0.3, 3.5, 1],
 [1.01, 0.8, 3.5, 2],
 [2.02, 1.2, 3.6, 0],
 [3.03, 1.4, 3.7, 3],
 [4.04, 1.9, 3.1, 0],
 [5.05, 2.3, 2.5, 5],
 [6.06, 2.8, 4, 1],
 [7.00, 2.9, 1, 3],
 [10.5, 3.2, 1, 2]
 ]
 */
public class Variant {

    private final BigDecimal light_slits_distance;
    private final BigDecimal light_screen_distance;
    private final BigDecimal[] light_screen_range;
    private final BigDecimal light_screen_step;
    private final BigDecimal light_width;
    private final BigDecimal[] light_width_range;
    private final BigDecimal light_width_step;
    private final BigDecimal light_length;
    private final BigDecimal[] light_length_range;
    private final BigDecimal light_length_step;
    private final boolean right_slit_closed;
    private final boolean left_slit_closed;
    private final BigDecimal between_slits_width;
    private final BigDecimal[] between_slits_range;
    private final BigDecimal between_slits_step;
    private final BigDecimal visibility;
    private final List<BigDecimal[]> data_plot_pattern;

    @JsonCreator
    public Variant(
            @JsonProperty("light_slits_distance") BigDecimal light_slits_distance,
            @JsonProperty("light_screen_distance") BigDecimal light_screen_distance,
            @JsonProperty("light_screen_range") BigDecimal[] light_screen_range,
            @JsonProperty("light_screen_step") BigDecimal light_screen_step,
            @JsonProperty("light_width") BigDecimal light_width,
            @JsonProperty("light_width_range") BigDecimal[] light_width_range,
            @JsonProperty("light_width_step") BigDecimal light_width_step,
            @JsonProperty("light_length") BigDecimal light_length,
            @JsonProperty("light_length_range") BigDecimal[] light_length_range,
            @JsonProperty("light_length_step") BigDecimal light_length_step,
            @JsonProperty("right_slit_closed") boolean right_slit_closed,
            @JsonProperty("left_slit_closed") boolean left_slit_closed,
            @JsonProperty("between_slits_width") BigDecimal between_slits_width,
            @JsonProperty("between_slits_range") BigDecimal[] between_slits_range,
            @JsonProperty("between_slits_step") BigDecimal between_slits_step,
            @JsonProperty("visibility") BigDecimal visibility,
            @JsonProperty("data_plot_pattern") List<BigDecimal[]> data_plot_pattern) {

        Objects.requireNonNull(light_slits_distance);
        Objects.requireNonNull(light_screen_distance);
        Objects.requireNonNull(light_screen_range);
        Objects.requireNonNull(light_screen_step);
        Objects.requireNonNull(light_width);
        Objects.requireNonNull(light_width_range);
        Objects.requireNonNull(light_width_step);
        Objects.requireNonNull(light_length);
        Objects.requireNonNull(light_length_range);
        Objects.requireNonNull(light_length_step);
        Objects.requireNonNull(right_slit_closed);
        Objects.requireNonNull(left_slit_closed);
        Objects.requireNonNull(between_slits_width);
        Objects.requireNonNull(between_slits_range);
        Objects.requireNonNull(between_slits_step);
        Objects.requireNonNull(visibility);
        Objects.requireNonNull(data_plot_pattern);


        if (light_screen_range.length != 2){
            throw new IllegalArgumentException("Range should have 2 elements but there was " + light_length_range.length);
        }
        if (light_width_range.length != 2){
            throw new IllegalArgumentException("Range should have 2 elements but there was " + light_width_range.length);
        }
        if (light_length_range.length != 2){
            throw new IllegalArgumentException("Range should have 2 elements but there was " + light_length_range.length);
        }
        if (between_slits_range.length != 2){
            throw new IllegalArgumentException("Range should have 2 elements but there was " + between_slits_range.length);
        }


        this.light_slits_distance = shrink(light_slits_distance);
        this.light_screen_distance = shrink(light_screen_distance);
        this.light_screen_range = shrink(light_screen_range);
        this.light_screen_step = shrink(light_screen_step);
        this.light_width = shrink(light_width);
        this.light_width_range = shrink(light_width_range);
        this.light_width_step = shrink(light_width_step);
        this.light_length = shrink(light_length);
        this.light_length_range = shrink(light_length_range);
        this.light_length_step = shrink(light_length_step);
        this.right_slit_closed = right_slit_closed;
        this.left_slit_closed = left_slit_closed;
        this.between_slits_width = shrink(between_slits_width);
        this.between_slits_range = shrink(between_slits_range);
        this.between_slits_step = shrink(between_slits_step);
        this.data_plot_pattern = shrink(data_plot_pattern);
        this.visibility = shrink(visibility);
    }

    public BigDecimal getLight_slits_distance() {
        return light_slits_distance;
    }

    public BigDecimal getLight_screen_distance() {
        return light_screen_distance;
    }

    public BigDecimal[] getLight_screen_range() {
        return light_screen_range;
    }

    public BigDecimal getLight_screen_step() {
        return light_screen_step;
    }

    public BigDecimal getLight_width() {
        return light_width;
    }

    public BigDecimal[] getLight_width_range() {
        return light_width_range;
    }

    public BigDecimal getLight_width_step() {
        return light_width_step;
    }

    public BigDecimal getLight_length() {
        return light_length;
    }

    public BigDecimal[] getLight_length_range() {
        return light_length_range;
    }

    public BigDecimal getLight_length_step() {
        return light_length_step;
    }

    public boolean isRight_slit_closed() {
        return right_slit_closed;
    }

    public boolean isLeft_slit_closed() {
        return left_slit_closed;
    }

    public BigDecimal getBetween_slits_width() {
        return between_slits_width;
    }

    public BigDecimal[] getBetween_slits_range() {
        return between_slits_range;
    }

    public BigDecimal getBetween_slits_step() {
        return between_slits_step;
    }

    public BigDecimal getVisibility() {
        return visibility;
    }

    public List<BigDecimal[]> getData_plot_pattern() {
        return data_plot_pattern;
    }

}
