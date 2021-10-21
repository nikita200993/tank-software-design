package ru.mipt.bit.platformer.driver.initalizers;

import java.util.List;
import java.util.Random;

import ru.mipt.bit.platformer.driver.GameLevelInitializer;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Point2D;

public class RandomGameLevelInitializer implements GameLevelInitializer {

    private final Random random;
    private final int obstacles;
    private final int enemies;

    public RandomGameLevelInitializer(final int obstacles, final int enemies) {
        this(new Random(), obstacles, enemies);
    }

    RandomGameLevelInitializer(final Random random, final int obstacles, final int enemies) {
        this.random = random;
        this.obstacles = obstacles;
        this.enemies = enemies;
    }

    @Override
    public Level init(final int width, final int height) {
        final int pointsNumber = obstacles + enemies + 1;
        if (pointsNumber > width * height) {
            throw new IllegalArgumentException(
                    "Level can't contain more than " + width * height + " objects, but was " + pointsNumber
            );
        }
        final List<Point2D> points = new RandomPointsGenerator(random, width, height)
                .generatePoints(pointsNumber);
        return new Level(
                points.get(0),
                points.subList(1, 1 + enemies),
                points.subList(1 + enemies, points.size()),
                width,
                height
        );
    }
}
