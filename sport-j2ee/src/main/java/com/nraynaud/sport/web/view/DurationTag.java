package com.nraynaud.sport.web.view;

import static com.nraynaud.sport.web.converter.DurationConverter.formatDuration;

public class DurationTag extends FormattingTagSupport<Long> {

    protected String formatValue(final Long duration) {
        return formatDuration(duration, new String[]{"h", "\'", "''"});
    }
}
