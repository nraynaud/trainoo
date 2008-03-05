package com.nraynaud.sport.web.view;

public class DistanceTag extends FormattingTagSupport<Double> {

    protected String formatValue(final Double distance) {
        return Helpers.formatDistance(distance);
    }
}
