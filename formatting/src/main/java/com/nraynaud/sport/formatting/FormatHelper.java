package com.nraynaud.sport.formatting;

public class FormatHelper {
    private FormatHelper() {
    }

    public static String formatDistance(final Double distance, final String ifNull) {
        return distance != null ? DistanceIO.formatDistance(distance) + "km" : ifNull;
    }

    public static String formatEnergy(final Long energy, final String ifNull) {
        return energy != null ? EnergyIO.formatEnergy(energy) + "kcal" : ifNull;
    }

    public static String formatDuration(final Long duration, final String ifNull) {
        return duration != null ? DurationIO.formatDuration(duration, "h", "\'", "''") : ifNull;
    }
}
