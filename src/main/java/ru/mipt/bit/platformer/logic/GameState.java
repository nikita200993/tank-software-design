package ru.mipt.bit.platformer.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private final Tank player;
    private final List<Tank> aiTanks;
    private final List<Point2D> obstacles;
    private final List<Colliding> collidingObjects;
    private final int width;
    private final int height;

    private GameState(
            final Tank player,
            final List<Tank> aiTanks,
            final List<Point2D> obstacles,
            final List<Colliding> collidingObjects,
            final int width,
            final int height
    ) {
        this.player = player;
        this.aiTanks = aiTanks;
        this.obstacles = obstacles;
        this.collidingObjects = collidingObjects;
        this.width = width;
        this.height = height;
    }

    public static GameState create(final Level level) {
        final var player = new Tank(level.getPlayerCoordinate());
        final var aiTanks = level.getAiPlayers()
                .stream()
                .map(Tank::new)
                .collect(Collectors.toList());
        final var collidingObjects = new ArrayList<Colliding>(aiTanks);
        collidingObjects.add(player);
        level.getTreesCoordinates()
                .stream()
                .map(SinglePoint::new)
                .forEach(collidingObjects::add);
        collidingObjects.add(new RectangleMap(level.getWidth(), level.getHeight()));
        return new GameState(
                player,
                aiTanks,
                level.getTreesCoordinates(),
                collidingObjects,
                level.getWidth(),
                level.getHeight()
        );
    }

    public List<MoveView> getMoveViews() {
        final var moveViews = new ArrayList<MoveView>(aiTanks);
        moveViews.add(player);
        return moveViews;
    }

    public Tank getPlayer() {
        return player;
    }

    public List<Tank> getAiTanks() {
        return aiTanks;
    }

    public List<Point2D> getObstacles() {
        return obstacles;
    }

    public List<Colliding> getCollidingObjects() {
        return collidingObjects;
    }


    public void update(final float deltaTime) {
        player.updateProgress(deltaTime);
        aiTanks.forEach(it -> it.updateProgress(deltaTime));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
