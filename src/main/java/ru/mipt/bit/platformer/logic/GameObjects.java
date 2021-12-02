package ru.mipt.bit.platformer.logic;


import java.util.List;

import ru.mipt.bit.platformer.logic.shoot.Bullet;

/**
 * Domain entity.
 */
public class GameObjects {
    private final Tank player;
    private final List<Tank> aiTanks;
    private final List<Obstacle> obstacles;
    private final List<Bullet> bullets;

    public GameObjects(
            final Tank player,
            final List<Tank> aiTanks,
            final List<Obstacle> obstacles,
            final List<Bullet> bullets
    ) {
        this.player = player;
        this.aiTanks = aiTanks;
        this.obstacles = obstacles;
        this.bullets = bullets;
    }

    public Tank getPlayer() {
        return player;
    }

    public List<Tank> getAiTanks() {
        return aiTanks;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    void addBullet(final Bullet bullet) {
        this.bullets.add(bullet);
    }

    void removeDeadObject(final Object deadObject) {
        aiTanks.remove(deadObject);
        bullets.remove(deadObject);
    }
}
