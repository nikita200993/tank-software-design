package ru.mipt.bit.platformer.logic;

import java.util.ArrayList;
import java.util.List;

import ru.mipt.bit.platformer.logic.shoot.Bullet;

/**
 * Domain entity.
 */
public class Level {
    private final GameObjects gameObjects;
    private final RectangleMap rectangleMap;
    private final DeathService deathService;
    private final List<GameLogicListener> listeners;

    public Level(
            final GameObjects gameObjects,
            final int width,
            final int height
    ) {
        this.gameObjects = gameObjects;
        this.rectangleMap = new RectangleMap(width, height);
        this.deathService = new DeathService();
        this.listeners = new ArrayList<>();
    }

    public void shoot(Tank tank) {
        tank.shoot().ifPresent(this::addBullet);
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
        if (getPlayer().isAlive()) {
            notifyCreation(getPlayer());
        }
        getAiTanks().forEach(this::notifyCreation);
        getBullets().forEach(this::notifyCreation);
        getObstacles().forEach(this::notifyCreation);
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

    void addBullet(final Bullet bullet) {
        if (rectangleMap.collides(bullet.position())) {
            return;
        }
        this.gameObjects.addBullet(bullet);
        notifyCreation(bullet);
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
        for (final var death : deaths) {
            gameObjects.removeDeadObject(death.getBullet());
            death.getBullet().update(death.getDeathTime());
            notifyDeath(death.getBullet());
            if (death.getTank().isEmpty()) {
                continue;
            }
            var tank = death.getTank().orElseThrow();
            gameObjects.removeDeadObject(tank);
            tank.update(death.getDeathTime());
            notifyDeath(tank);
        }
    }

    private void notifyCreation(GameObjectView gameObjectView) {
        listeners.forEach(it -> it.onEvent(new CreationEvent(gameObjectView)));
    }

    private void notifyDeath(GameObjectView gameObjectView) {
        listeners.forEach(it -> it.onEvent(new DeathEvent(gameObjectView)));
    }
}
