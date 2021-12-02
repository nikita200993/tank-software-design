package ru.mipt.bit.platformer.logic;

import java.util.List;

/**
 * Domain entity.
 */
public class Level {
    private final Point2D playerCoordinate;
    private final List<Point2D> aiPlayers;
    private final List<Point2D> treesCoordinates;
    private final int width;
    private final int height;

    public Level(
            final Point2D playerCoordinate,
            final List<Point2D> aiPlayers,
            final List<Point2D> treesCoordinates,
            final int width,
            final int height
    ) {
        this.playerCoordinate = playerCoordinate;
        this.aiPlayers = aiPlayers;
        this.treesCoordinates = treesCoordinates;
        this.width = width;
        this.height = height;
    }

    public List<Point2D> getTreesCoordinates() {
        return treesCoordinates;
    }

    public Point2D getPlayerCoordinate() {
        return playerCoordinate;
    }

    public List<Point2D> getAiPlayers() {
        return aiPlayers;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
