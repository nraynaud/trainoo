package com.nraynaud.sport.web.view;

public class DurationTag extends FormattingTagSupport<Long> {

    protected String formatValue(final Long duration) {
        return Helpers.formatDuration(duration);
    }
}
