package com.nraynaud.sport.nikeplus;

import static com.nraynaud.sport.formatting.DistanceIO.formatDistance;
import static com.nraynaud.sport.formatting.DurationIO.formatDuration;
import com.nraynaud.sport.nikeplus.data.Workout;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;

public class NikeGraphDrawer {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 150;
    private static final double MIN_VARIANCE = 200000.0;
    private static final double DAMPING_FACTOR = 2.0;
    private static final double TO_MILES = 1.609344;
    private static final int X_PADDING = 15;
    private static final AffineTransform SHADOWER = AffineTransform.getTranslateInstance(3, 2);
    private static final int Y_PADDING = 25;
    private static final Color TEXT_COLOR = new Color(64, 54, 21);
    private static final Font TEXT_FONT = new Font("Dialog", Font.BOLD, 12);
    private static final Color SNAP_FILL_COLOR = new Color(247, 247, 226);
    private static final Color SNAP_RING_COLOR = new Color(113, 139, 7);
    private static final Color LIGHT_GREEN = new Color(231, 243, 185);
    private static final Color DARK_GREEN = new Color(161, 189, 61);
    private static final Color SHADOWS_COLOR = new Color(163, 163, 130, 150);

    private NikeGraphDrawer() {
    }

    public static byte[] getPNGImage(final Workout workout) {
        try {
            final double variance = computeVariance(workout.minPace, workout.maxPace);
            final double distanceCoeff = (WIDTH - X_PADDING * 2) / workout.points.last().distance;
            final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            drawInfo(g, workout);
            drawCurve(g, workout.points, workout.minPace, variance, distanceCoeff);
            drawSnaps(g, workout.minPace, variance, distanceCoeff, workout.snapshots);
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "PNG", buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void drawInfo(final Graphics2D g, final Workout workout) throws
            IOException {
        final BufferedImage logoData = ImageIO.read(
                NikeGraphDrawer.class.getClassLoader().getResource("/com/nraynaud/sport/nikeplus/logo_widget.png"));
        final int logoHeight = logoData.getHeight();
        final int logoWidth = logoData.getWidth();
        paintRows(g);
        paintBorder(g);
        g.drawImage(logoData, WIDTH - logoWidth - 4, HEIGHT - logoHeight - 4, logoWidth, logoHeight, null);
        g.setPaint(TEXT_COLOR);
        g.setFont(TEXT_FONT);
        final String averageSpeed = formatDistance(workout.distance / (workout.duration / 3600.0)) + "km/h";
        final String topText = workout.login + "   " + new SimpleDateFormat("dd/MM/yyyy").format(workout.date);
        g.drawString(topText + "  " + averageSpeed, X_PADDING / 2, 19);
        final String bottomText = formatDistance(workout.distance) + "km - " + formatDuration(
                workout.duration, "h", "'", "''");
        g.drawString(bottomText, X_PADDING / 2, HEIGHT - 7);
    }

    private static void paintRows(final Graphics2D g) {
        final int rowHeight = HEIGHT / 6;
        for (int i = 0; i < 6; i++) {
            g.setPaint(i % 2 == 0 ? Color.WHITE : LIGHT_GREEN);
            final int currentHeight = i * rowHeight;
            g.fillRect(0, currentHeight, WIDTH, rowHeight);
            g.setPaint(DARK_GREEN);
            g.drawLine(0, currentHeight, WIDTH, currentHeight);
        }
    }

    private static void paintBorder(final Graphics2D g) {
        g.setPaint(DARK_GREEN);
        g.drawLine(0, 0, 0, HEIGHT);
        g.drawLine(WIDTH - 1, 0, WIDTH - 1, HEIGHT);
        g.setStroke(new BasicStroke(3));
        g.drawLine(0, 1, WIDTH, 1);
        g.drawLine(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
    }

    private static void drawCurve(final Graphics2D g, final SortedSet<Workout.Point> points,
                                  final double min, final double variance,
                                  final double distanceCoeff) {
        final java.util.List<Workout.Point> list = new ArrayList<Workout.Point>(points);
        final Path2D.Double path = new Path2D.Double();
        path.moveTo(x(distanceCoeff, points.first()), y(min, variance, points.first()));
        for (int i = 1; i < list.size(); i++) {
            final Workout.Point point = list.get(i);
            final Workout.Point next = list.get(i + 1 < list.size() ? i + 1 : i);
            final int ctrlX = x(distanceCoeff, point);
            final int ctrlY = y(min, variance, point);
            final int x = (int) (ctrlX + (x(distanceCoeff, next) - ctrlX) / 2.0);
            final int y = (int) (ctrlY + (y(min, variance, next) - ctrlY) / 2.0);
            path.quadTo(ctrlX, ctrlY, x, y);
        }
        g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setPaint(SHADOWS_COLOR);
        g.draw(SHADOWER.createTransformedShape(path));
        g.setPaint(DARK_GREEN);
        g.draw(path);
    }

    private static void drawSnaps(final Graphics2D g, final double min, final double variance,
                                  final double distanceCoeff,
                                  final Collection<Workout.Point> list) {
        g.setStroke(new BasicStroke(2));
        for (final Workout.Point point : list) {
            final int left = x(distanceCoeff, point) - 3;
            final int top = y(min, variance, point) - 3;
            final Ellipse2D.Float ellipse = new Ellipse2D.Float(left, top, 7, 7);
            g.setPaint(SHADOWS_COLOR);
            g.fill(SHADOWER.createTransformedShape(ellipse));
            g.setPaint(SNAP_FILL_COLOR);
            g.fill(ellipse);
            g.setPaint(SNAP_RING_COLOR);
            g.draw(ellipse);
        }
    }

    private static double computeVariance(final double min, final double max) {
        final double actualVariance = milesPace(max - min);
        final double variance = Math.max(actualVariance, MIN_VARIANCE);
        if (variance == MIN_VARIANCE) {
            return variance + DAMPING_FACTOR * (MIN_VARIANCE - variance);
        }
        return variance;
    }

    private static double milesPace(final double pace) {
        return pace * TO_MILES;
    }

    private static int y(final double min, final double variance, final Workout.Point point) {
        final double diff = point.pace - min;
        return (int) (Y_PADDING + (diff / variance) * HEIGHT);
    }

    private static int x(final double distanceCoeff, final Workout.Point previous) {
        return (int) (previous.distance * distanceCoeff) + X_PADDING;
    }
}
