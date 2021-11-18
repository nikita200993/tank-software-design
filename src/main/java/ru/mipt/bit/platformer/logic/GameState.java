package ru.mipt.bit.platformer.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.mipt.bit.platformer.logic.shoot.Bullet;

public class GameState {
    private final GameObjects gameObjects;
    private final RectangleMap rectangleMap;
    private final DeathService deathService;
    private final List<GameLogicListener> listeners;

    private GameState(
            final GameObjects gameObjects,
            final int width,
            final int height
    ) {
        this.gameObjects = gameObjects;
        this.rectangleMap = new RectangleMap(width, height);
        this.deathService = new DeathService();
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

    public boolean isPlayerAlive() {
        return getPlayer().isAlive();
    }

    public void update(final float timeTick) {
        processDeathsOnThisStep(timeTick);
        updateSurvived(timeTick);
    }

    public void addListener(final GameLogicListener listener) {
        listeners.add(listener);
        final var tanks = new ArrayList<GameObjectView>(gameObjects.getAiTanks());
        if (getPlayer().isAlive()) {
            tanks.add(getPlayer());
        }
        listener.onRegister(
                tanks,
                Collections.unmodifiableList(gameObjects.getObstacles()),
                Collections.unmodifiableList(gameObjects.getBullets())
        );
    }

    public Tank getPlayer() {
        return gameObjects.getPlayer();
    }

    public List<Tank> getAiTanks() {
        return gameObjects.getAiTanks();
    }

    public List<Obstacle> getObstacles() {
        return gameObjects.getObstacles();
    }

    public List<Bullet> getBullets() {
        return gameObjects.getBullets();
    }

    public List<Colliding> getCollidingObjects() {
        final var collidingObjects = new ArrayList<Colliding>(gameObjects.getAiTanks());
        if (getPlayer().isAlive()) {
            collidingObjects.add(gameObjects.getPlayer());
        }
        collidingObjects.addAll(gameObjects.getObstacles());
        collidingObjects.add(rectangleMap);
        return collidingObjects;
    }

    public int getWidth() {
        return rectangleMap.getWidth();
    }

    public int getHeight() {
        return rectangleMap.getHeight();
    }

    public RectangleMap getRectangleMap() {
        return rectangleMap;
    }

    static GameState create(
            final Tank player,
            final List<Tank> aiTanks,
            final List<Point2D> obstaclePoints,
            final int width,
            final int height
    ) {
        final var obstacles = obstaclePoints.stream()
                .map(Obstacle::new)
                .collect(Collectors.toList());
        return new GameState(
                new GameObjects(player,
                        aiTanks,
                        obstacles,
                        new ArrayList<>()
                ),
                width,
                height
        );
    }

    void addBullet(final Bullet bullet) {
        if (rectangleMap.collides(bullet.position())) {
            return;
        }
        this.gameObjects.addBullet(bullet);
        notifyOnBulletCreated(bullet);
    }

    private void notifyOnBulletCreated(final Bullet bullet) {
        listeners.forEach(it -> it.onBulletCreated(bullet));
    }

    private void updateSurvived(final float deltaTime) {
        if (getPlayer().isAlive()) {
            getPlayer().update(deltaTime);
        }
        getAiTanks().forEach(it -> it.update(deltaTime));
        getBullets().forEach(it -> it.update(deltaTime));
    }

    private void processDeathsOnThisStep(final float timeTick) {
        final var deaths = deathService.computeDeathsFromHits(this, timeTick);
        final var deadTanks = new ArrayList<Tank>();
        final var deadBullets = new ArrayList<Bullet>();
        for (final var death : deaths) {
            processBulletDeath(death.getBullet(), death.getDeathTime(), deadBullets);
            death.getTank()
                    .ifPresent(it -> processTankDeath(it, death.getDeathTime(), deadTanks));
        }
        notifyOnDeath(deadTanks, deadBullets);
    }

    private void processTankDeath(final Tank tank, final float deathTime, final List<Tank> deadTanks) {
        gameObjects.removeDeadObject(tank);
        tank.update(deathTime);
        deadTanks.add(tank);
    }

    private void processBulletDeath(final Bullet bullet, final float deathTime, final List<Bullet> bullets) {
        gameObjects.removeDeadObject(bullet);
        bullet.update(deathTime);
        bullets.add(bullet);
    }

    private void notifyOnDeath(final List<Tank> deadTanks, final List<Bullet> deadBullets) {
        for (final var listener : listeners) {
            listener.onBulletsDeath(deadBullets);
            listener.onTanksDeath(deadTanks);
        }
    }
}
