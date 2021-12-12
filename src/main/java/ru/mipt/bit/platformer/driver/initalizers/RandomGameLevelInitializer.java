package ru.mipt.bit.platformer.driver.initalizers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.mipt.bit.platformer.driver.GameLevelInitializer;
import ru.mipt.bit.platformer.logic.GameObjects;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Obstacle;
import ru.mipt.bit.platformer.logic.Point2D;
import ru.mipt.bit.platformer.logic.Tank;

/**
 * Input adapter.
 */
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
        var player = new Tank(points.get(0));
        var bots = new ArrayList<Tank>();
        for (var botPosition : points.subList(1, 1 + enemies)) {
            bots.add(new Tank(botPosition));
        }
        var obstacles = new ArrayList<Obstacle>();
        for (var obstaclePosition : points.subList(1 + enemies, points.size())) {
            obstacles.add(new Obstacle(obstaclePosition));
        }
        return new Level(
                new GameObjects(
                        player,
                        bots,
                        obstacles,
                        new ArrayList<>()
                ),
                width,
                height
        );
    }
}
