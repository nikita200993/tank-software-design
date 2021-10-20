package ru.mipt.bit.platformer.driver.initalizers;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
        final List<Point2D> points = new RandomPointsGenerator(random, width, height)
                .generatePoints(1, maxObstacles + 1);
        return new GameLogic(
                new Player(points.get(0)),
                points.stream()
                        .skip(1)
                        .collect(Collectors.toList())
        );
    }
}
