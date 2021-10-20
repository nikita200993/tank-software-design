package ru.mipt.bit.platformer.driver.initalizers;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ru.mipt.bit.platformer.logic.Point2D;

public class RandomPointsGenerator {

    private final Random random;
    private final int width;
    private final int height;

    public RandomPointsGenerator(final Random random, final int width, final int height) {
        this.random = random;
        this.width = width;
        this.height = height;
    }

    public List<Point2D> generatePoints(final int minPointsNumber, final int maxPointsNumber) {
        checkNotNegative(maxPointsNumber);
        checkMinPointsNumberNotGreater(minPointsNumber, maxPointsNumber);
        final int pointsNumber = Math.max(minPointsNumber, random.nextInt(maxPointsNumber + 1));
        final Set<Point2D> points = new LinkedHashSet<>();
        for (int i = 0; i < pointsNumber; i++) {
            points.add(generateNextPoint(points));
        }
        return List.copyOf(points);
    }

    private static void checkMinPointsNumberNotGreater(final int minPointsNumber, final int maxPointsNumber) {
        if (minPointsNumber > maxPointsNumber) {
            throw new IllegalArgumentException(
                    String.format(
                            "minimum point number must be not greater than max point number,"
                                    + " but were min = %d, max = %d",
                            minPointsNumber,
                            maxPointsNumber
                    )
            );
        }
    }

    private static void checkNotNegative(final int maxPointsNumber) {
        if (maxPointsNumber < 0) {
            throw new IllegalArgumentException("max point number should be non negative, but was " + maxPointsNumber);
        }
    }


    private Point2D generateNextPoint(final Set<Point2D> obstacles) {
        while (true) {
            final Point2D point = new Point2D(random.nextInt(width), random.nextInt(height));
            if (!obstacles.contains(point)) {
                return point;
            }
        }
    }
}
