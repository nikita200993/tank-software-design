package ru.mipt.bit.platformer.logic;

import java.util.List;

import ru.mipt.bit.platformer.logic.shoot.Canon;

public class Tank implements Colliding, GameObjectView {
    private final TankMove tankMove;
    private final Canon canon;
    private final int maxHealth;
    private int health;
    private TankState tankState;

    Tank(Point2D currentPosition, Canon canon) {
        this.tankMove = new TankMove(currentPosition);
        this.canon = canon;
        maxHealth = 100;
        health = maxHealth;
        tankState = new FreshState(this);
    }

    public Tank(Point2D currentPosition) {
        this(currentPosition, new Canon(0.5f, 20, 4.0f));
    }

    @Override
    public boolean collides(Point2D point2D) {
        return point2D.equals(tankMove.getCurrentPosition())
                || point2D.equals(tankMove.getDestinationPosition());
    }

    @Override
    public FloatPoint2D position() {
        var currentPosition = tankMove.getCurrentPosition();
        var destinationPosition = tankMove.getDestinationPosition();
        float moveProgress = tankMove.getMoveProgress();
        return new FloatPoint2D(
                currentPosition.getX() + (destinationPosition.getX() - currentPosition.getX()) * moveProgress,
                currentPosition.getY() + (destinationPosition.getY() - currentPosition.getY()) * moveProgress
        );
    }

    @Override
    public float angle() {
        return tankMove.getDirection().getAngle();
    }

    public void update(float time) {
        tankState.update(time);
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public void shoot(Level level) {
        tankState.shoot(level);
    }

    public Point2D currentPosition() {
        return tankMove.getCurrentPosition().copy();
    }

    public void reduceHealth(int damage) {
        tankState.reduceHealth(damage);
    }

    public Point2D destinationPosition() {
        return tankMove.getDestinationPosition().copy();
    }

    public Direction getDirection() {
        return tankMove.getDirection();
    }

    public int getHealthPercent() {
        return Math.round(Math.max(0, health / (float) maxHealth) * 100);
    }

    public void startMove(Direction direction, List<Colliding> collidings) {
        tankState.startMove(direction, collidings);
    }

    float getMoveSpeed() {
        return tankState.moveSpeed();
    }

    float getNominalSpeed() {
        return 2.5f;
    }

    float getMoveProgress() {
        return tankMove.getMoveProgress();
    }

    void setHealth(int health) {
        this.health = health;
    }

    void setTankState(TankState tankState) {
        this.tankState = tankState;
    }

    Canon getCanon() {
        return canon;
    }

    TankMove getTankMove() {
        return tankMove;
    }

    int getHealth() {
        return health;
    }
}
