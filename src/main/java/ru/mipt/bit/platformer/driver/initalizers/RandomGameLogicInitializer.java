package ru.mipt.bit.platformer.driver.initalizers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ru.mipt.bit.platformer.driver.GameLogicInitializer;
import ru.mipt.bit.platformer.logic.GameLogic;
import ru.mipt.bit.platformer.logic.Player;
import ru.mipt.bit.platformer.logic.Point2D;

public class RandomGameLogicInitializer implements GameLogicInitializer {

    private final Random random;
    private final float maxObstaclesShare;

    public RandomGameLogicInitializer() {
        this(new Random(), 0.25f);
    }

    RandomGameLogicInitializer(final Random random, final float maxObstaclesShare) {
        this.random = random;
        this.maxObstaclesShare = maxObstaclesShare;
    }

    @Override
    public GameLogic init(final Map<String, Object> levelProps) {
        final int width = (int) levelProps.get("width");
        final int height = (int) levelProps.get("height");
        final int tilesCount = width * height;
        final int maxObstacles = Math.min(tilesCount - 1, (int) Math.floor(tilesCount * maxObstaclesShare));
        final Set<Point2D> obstacles = new HashSet<>();
        final int obstacleCount = random.nextInt(maxObstacles) + 1;
        for (int i = 0; i < obstacleCount; i++) {
            obstacles.add(generateNextPoint(obstacles, random, tilesCount, width));
        }
        final Point2D playerPosition = generateNextPoint(obstacles, random, tilesCount, width);
        return new GameLogic(new Player(playerPosition), List.copyOf(obstacles));
    }

    private static Point2D generateNextPoint(
            final Set<Point2D> obstacles,
            final Random random,
            final int limit,
            final int width
    ) {
        while (true) {
            final int nextInt = random.nextInt(limit);
            final Point2D point = new Point2D(nextInt / width, nextInt % width);
            if (!obstacles.contains(point)) {
                return point;
            }
        }
    }
}
