package com.nraynaud.sport.formatting;

public class FormatHelper {
    private FormatHelper() {
    }

    public static String formatDistanceHtml(final Double distance, final String ifNull) {
        return distance != null ? DistanceIO.formatDistance(distance) + "<small>km</small>" : ifNull;
    }

    public static String formatDistance(final Double distance, final boolean showUnit, final String ifNull) {
        final String unit = showUnit ? "km" : "";
        return distance != null ? DistanceIO.formatDistance(distance) + unit : ifNull;
    }

    public static String formatEnergy(final Long energy, final boolean showUnit, final String ifNull) {
        final String unit = showUnit ? "<small>kcal</small>" : "";
        return energy != null ? EnergyIO.formatEnergy(energy) + unit : ifNull;
    }

    public static String formatDuration(final Long duration, final String ifNull) {
        return duration != null ? DurationIO.formatDuration(duration, "h", "\'", "''") : ifNull;
    }
}
