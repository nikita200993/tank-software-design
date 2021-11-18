package ru.mipt.bit.platformer.logic;

import java.util.List;
import java.util.Optional;

import ru.mipt.bit.platformer.logic.shoot.Bullet;
import ru.mipt.bit.platformer.logic.shoot.Canon;

public class Tank implements Colliding, GameObject {

    private final static float MOVE_SPEED = 2.5f;

    private final Point2D currentPosition;
    private final Point2D destinationPosition;
    private float moveProgress;
    private Direction direction;
    private int health;
    private final Canon canon;

    Tank(final Point2D currentPosition, final Canon canon) {
        this.currentPosition = currentPosition;
        this.destinationPosition = currentPosition.copy();
        this.moveProgress = 1;
        this.direction = Direction.RIGHT;
        this.health = 100;
        this.canon = canon;
    }

    Tank(final Point2D currentPosition) {
        this(currentPosition, new Canon(0.5f, 20, 4.0f));
    }

    @Override
    public void update(final float time) {
        canon.update(time);
        moveProgress = Math.min(moveProgress + time * getMoveSpeed(), 1);
        if (finishedMove()) {
            currentPosition.set(destinationPosition);
        }
    }

    @Override
    public boolean collides(final Point2D point2D) {
        if (isMoving()) {
            return point2D.equals(currentPosition) || point2D.equals(destinationPosition);
        } else {
            return point2D.equals(currentPosition);
        }
    }

    @Override
    public FloatPoint2D position() {
        return new FloatPoint2D(
                currentPosition.getX() + (destinationPosition.getX() - currentPosition.getX()) * moveProgress,
                currentPosition.getY() + (destinationPosition.getY() - currentPosition.getY()) * moveProgress
        );
    }

    public Optional<Bullet> shoot() {
        return canon.shoot(
                direction,
                position().plus(
                        FloatPoint2D.from(direction.unitVector()).multiply(0.5f)
                )
        );
    }

    public float getMoveProgress() {
        return moveProgress;
    }

    @Override
    public float angle() {
        return direction.getAngle();
    }

    public Point2D currentPosition() {
        return currentPosition.copy();
    }

    public Point2D destinationPosition() {
        return destinationPosition.copy();
    }

    public Direction getDirection() {
        return direction;
    }

    public void reduceHealth(final int damage) {
        this.health -= damage;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public float getMoveSpeed() {
        return MOVE_SPEED;
    }

    public boolean isMoving() {
        return moveProgress != 1;
    }

    void startMove(final Direction direction, List<Colliding> obstacles) {
        if (isMoving()) {
            return;
        }
        this.direction = direction;
        final Point2D destination = direction.computeNextPosition(currentPosition);
        if (obstacles.stream().noneMatch(obstacle -> obstacle.collides(destination))) {
            moveProgress = 0;
            destinationPosition.set(destination);
        }
    }

    private boolean finishedMove() {
        return moveProgress == 1;
    }
}
