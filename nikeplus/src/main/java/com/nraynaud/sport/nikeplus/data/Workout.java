package com.nraynaud.sport.nikeplus.data;

import java.util.Date;
import java.util.SortedSet;

public class Workout {
    public final String login;
    public final Date date;
    public final SortedSet<Point> points;
    public final SortedSet<Point> snapshots;
    public final double distance;
    public final long duration;
    public final long energy;
    public final double minPace;
    public final double maxPace;

    public Workout(final String login, final Date date, final SortedSet<Point> points, final SortedSet<Point> snapshots,
                   final double distance, final long duration, final long energy, final double minPace,
                   final double maxPace) {
        this.login = login;
        this.date = date;
        this.points = points;
        this.snapshots = snapshots;
        this.distance = distance;
        this.duration = duration;
        this.energy = energy;
        this.minPace = minPace;
        this.maxPace = maxPace;
    }

    public static class Point {
        public final double distance;
        public double pace;

        public Point(final double distance, final double pace) {
            this.distance = distance;
            this.pace = pace;
        }

        public String toString() {
            return "[" + distance + ", " + pace + ']';
        }
    }
}
