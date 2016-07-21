package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static vlab.server_java.model.util.Util.shrink;

/**
 * Created by efimchick on 30.06.16.
 * var default_plot_data = [
 [0.01, 0, 3.5, 2],
 [1.01, 0.8, 3.5, 2],
 [2.02, 3, 6, 0],
 [3.03, 1.4, 0, 5],
 [4.04, 0.9, 3.1, 0],
 [5.05, 2.3, 2.5, 0],
 [6.06, 2.8, 8, 1],
 [7.00, 2, 1, 5],
 [10.5, 3.2, 1, 2]
 ];
 */
public class PlotData {
    private final List<BigDecimal[]> data_plot;
    private final BigDecimal visibility;

    @JsonCreator
    public PlotData(
            @JsonProperty("data_plot") List<BigDecimal[]> data_plot,
            @JsonProperty("visibility") BigDecimal visibility) {
        Objects.requireNonNull(data_plot);
        Objects.requireNonNull(visibility);
        this.data_plot = Collections.unmodifiableList(shrink(data_plot));
        this.visibility = shrink(visibility);
    }

    public List<BigDecimal[]> getData_plot() {
        return data_plot;
    }

    public BigDecimal getVisibility() {
        return visibility;
    }
}
