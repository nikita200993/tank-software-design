package ru.mipt.bit.platformer.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private final Tank player;
    private final List<Tank> aiTanks;
    private final List<Point2D> obstacles;
    private final List<Colliding> collidingObjects;
    private final int width;
    private final int height;
    private final List<GameLogicListener> listeners;

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
        this.listeners = new ArrayList<>();
    }

    public static GameState create(final Level level) {
        final var player = new Tank(level.getPlayerCoordinate());
        final var aiTanks = level.getAiPlayers()
                .stream()
                .map(Tank::new)
                .collect(Collectors.toList());
        return create(
                player,
                aiTanks,
                level.getTreesCoordinates(),
                level.getWidth(),
                level.getHeight()
        );
    }

    static GameState create(
            final Tank player,
            final List<Tank> aiTanks,
            final List<Point2D> obstacles,
            final int width,
            final int height
    ) {
        final var collidingObjects = new ArrayList<Colliding>(aiTanks);
        collidingObjects.add(player);
        obstacles.stream()
                .map(SinglePoint::new)
                .forEach(collidingObjects::add);
        collidingObjects.add(new RectangleMap(width, height));
        return new GameState(
                player,
                aiTanks,
                obstacles,
                collidingObjects,
                width,
                height
        );
    }

    public void update(final float deltaTime) {
        player.updateProgress(deltaTime);
        aiTanks.forEach(it -> it.updateProgress(deltaTime));
    }

    public void addListener(final GameLogicListener listener) {
        listeners.add(listener);
        final var tanks = new ArrayList<>(aiTanks);
        tanks.add(player);
        listener.onRegister(
                Collections.unmodifiableList(tanks),
                obstacles.stream()
                        .map(it -> new StaticGameObjectView(new FloatPoint2D(it.getX(), it.getY())))
                        .collect(Collectors.toList())
        );
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
