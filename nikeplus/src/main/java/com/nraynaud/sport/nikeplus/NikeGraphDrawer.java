package com.nraynaud.sport.nikeplus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;

public class NikeGraphDrawer {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 200;
    private static final double MIN_VARIANCE = 200000.0;
    private static final double DAMPING_FACTOR = 2.0;
    private static final double TO_MILES = 1.609344;
    private static final int X_PADDING = 15;

    private NikeGraphDrawer() {
    }

    public static byte[] getPNGImage(final SortedSet<NikeCurveHelper.Point> points, final URL logo) {
        try {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (final NikeCurveHelper.Point point : points) {
                min = Math.min(min, point.pace);
                max = Math.max(max, point.pace);
            }
            final double variance = computeVariance(min, max);
            final double distanceCoeff = (WIDTH - X_PADDING * 2) / points.last().distance;
            final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g = image.createGraphics();
            final AffineTransform shadower = AffineTransform.getTranslateInstance(3, 2);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            drawLogo(logo, g);
            drawCurve(points, min, variance, distanceCoeff, g, shadower);
            drawSnaps(min, variance, distanceCoeff, g, points);
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

    private static void drawLogo(final URL logo, final Graphics2D g) throws IOException {
        final BufferedImage logoData = ImageIO.read(logo);
        final int logoHeight = 25;
        final int logoWidth = logoHeight * logoData.getWidth() / logoData.getHeight();
        g.drawImage(logoData, 0, HEIGHT - logoHeight, logoWidth, logoHeight, null);
    }

    private static void drawCurve(final SortedSet<NikeCurveHelper.Point> points,
                                  final double min, final double variance,
                                  final double distanceCoeff, final Graphics2D g,
                                  final AffineTransform shadower) {
        g.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        final java.util.List<NikeCurveHelper.Point> list = new ArrayList<NikeCurveHelper.Point>(points);
        final Path2D.Double path = new Path2D.Double();
        path.moveTo(x(distanceCoeff, points.first()), y(min, variance, points.first()));
        for (int i = 1; i < list.size(); i++) {
            final NikeCurveHelper.Point point = list.get(i);
            final NikeCurveHelper.Point next = list.get(i + 1 < list.size() ? i + 1 : i);
            final int ctrlX = x(distanceCoeff, point);
            final int ctrlY = y(min, variance, point);
            final int x = (int) (ctrlX + (x(distanceCoeff, next) - ctrlX) / 2.0);
            final int y = (int) (ctrlY + (y(min, variance, next) - ctrlY) / 2.0);
            System.out.println("x: " + x + "\ty: " + y);
            path.quadTo(ctrlX, ctrlY, x, y);
        }
        g.setPaint(Color.GRAY);
        g.draw(shadower.createTransformedShape(path));
        g.setPaint(new Color(0x87DA06));
        g.draw(path);
    }

    private static void drawSnaps(final double min, final double variance, final double distanceCoeff,
                                  final Graphics2D g, final Collection<NikeCurveHelper.Point> list) {
        g.setStroke(new BasicStroke(2));
        final AffineTransform shadower = AffineTransform.getTranslateInstance(3, 2);
        for (final NikeCurveHelper.Point point : list) {
            final int left = x(distanceCoeff, point) - 3;
            final int top = y(min, variance, point) - 3;
            final Ellipse2D.Float ellipse = new Ellipse2D.Float(left, top, 7, 7);
            g.setPaint(Color.GRAY);
            g.fill(shadower.createTransformedShape(ellipse));
            g.setPaint(Color.WHITE);
            g.fill(ellipse);
            g.setPaint(Color.BLACK);
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

    private static int y(final double min, final double variance, final NikeCurveHelper.Point point) {
        final double diff = point.pace - min;
        return (int) (20 + (diff / variance) * HEIGHT);
    }

    private static int x(final double distanceCoeff, final NikeCurveHelper.Point previous) {
        return (int) (previous.distance * distanceCoeff) + X_PADDING;
    }
}
