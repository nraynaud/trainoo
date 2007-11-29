package com.nraynaud.sport.web.view;

import static com.nraynaud.sport.web.converter.DistanceConverter.formatNumber;

public class DistanceTag extends FormattingTagSupport<Double> {

    protected String formatValue(final Double distance) {
        return formatNumber(distance) + "km";
    }
}
