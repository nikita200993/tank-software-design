package ru.mipt.bit.platformer.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import ru.mipt.bit.platformer.logic.shoot.Bullet;

public class DeathService {

    public List<Death> computeDeathsFromHits(final GameState gameState, final float timeTick) {
        final var hits = collectHits(gameState, timeTick);
        return computeDeathsFromHits(hits);
    }

    private static List<Death> computeDeathsFromHits(final List<Hit> hits) {
        hits.sort(Comparator.comparing(Hit::getHitTime));
        final var deadSet = new HashSet<>();
        final var deaths = new ArrayList<Death>();
        for (final var hit : hits) {
            if (deadSet.contains(hit.getBullet()) || deadSet.contains(hit.getTank())) {
                continue;
            }
            hit.apply();
            deadSet.add(hit.getBullet());
            final var deadTankOptional = hit.getTank().filter(tank -> !tank.isAlive());
            if (deadTankOptional.isPresent()) {
                deadSet.add(deadTankOptional.orElseThrow());
                deaths.add(new Death(hit.getBullet(), deadTankOptional.orElseThrow(), hit.getHitTime()));
            } else {
                deaths.add(new Death(hit.getBullet(), null, hit.getHitTime()));
            }
        }
        return deaths;
    }

    private static List<Hit> collectHits(final GameState gameState, final float timeTick) {
        final var bullets = gameState.getBullets();
        final var tanks = gameState.getAiTanks();
        final var obstacles = gameState.getObstacles();
        final var rectangleMap = gameState.getRectangleMap();
        final var hits = new ArrayList<Hit>();
        for (final var bullet : bullets) {
            collectTankHits(timeTick, tanks, hits, bullet);
            collectObstacleHits(timeTick, obstacles, hits, bullet);
            collisionTime(rectangleMap, bullet, timeTick).ifPresent(
                    time -> hits.add(new Hit(bullet, null, time))
            );
        }
        return hits;
    }

    private static void collectObstacleHits(
            final float timeTick,
            final List<Obstacle> obstacles,
            final ArrayList<Hit> hits,
            final Bullet bullet
    ) {
        for (final var obstacle : obstacles) {
            collisionTime(obstacle, bullet, timeTick).ifPresent(
                    time -> hits.add(new Hit(bullet, null, time))
            );
        }
    }

    private static void collectTankHits(final float timeTick, final List<Tank> tanks, final ArrayList<Hit> hits, final Bullet bullet) {
        for (final var tank : tanks) {
            collisionTime(tank, bullet, timeTick).ifPresent(
                    time -> hits.add(new Hit(bullet, tank, time))
            );
        }
    }

    private static Optional<Float> collisionTime(
            final Tank tank,
            final Bullet bullet,
            final float timeTick
    ) {
        final FloatPoint2D tankCenter = tank.position().plus(0.5f);
        final FloatPoint2D tankSpeed = FloatPoint2D.from(tank.getDirection().unitVector())
                .multiply(tank.getMoveSpeed());
        final FloatPoint2D bulletCenter = bullet.position().plus(0.5f);
        final FloatPoint2D bulletSpeed = FloatPoint2D.from(bullet.getDirection().unitVector())
                .multiply(bullet.getSpeed());
        final float moveTime = Math.min(endOfMoveTime(tank), timeTick);
        return collisionTimeWithUnitCircle(bulletCenter, bulletSpeed, tankCenter, tankSpeed, moveTime)
                .or(
                        () -> collisionTimeWithUnitCircle(
                                bulletCenter.plus(bulletSpeed.multiply(moveTime)),
                                bulletSpeed,
                                tankCenter.plus(tankSpeed.multiply(moveTime)),
                                new FloatPoint2D(0, 0),
                                Math.max(0.0f, timeTick - moveTime)
                        )
                );
    }

    private static Optional<Float> collisionTime(
            final Obstacle obstacle,
            final Bullet bullet,
            final float timeTick
    ) {
        final FloatPoint2D obstacleCenter = obstacle.position().plus(0.5f);
        final FloatPoint2D obstacleSpeed = new FloatPoint2D(0, 0);
        final FloatPoint2D bulletCenter = bullet.position().plus(0.5f);
        final FloatPoint2D bulletSpeed = FloatPoint2D.from(bullet.getDirection().unitVector())
                .multiply(bullet.getSpeed());
        return collisionTimeWithUnitCircle(bulletCenter, bulletSpeed, obstacleCenter, obstacleSpeed, timeTick);
    }

    private static Optional<Float> collisionTime(
            final RectangleMap rectangleMap,
            final Bullet bullet,
            final float timeTick
    ) {
        final float collisionTime;
        final float yBullet = bullet.position().getY();
        final float xBullet = bullet.position().getX();
        final float yMax = rectangleMap.getHeight() - 1;
        final float xMax = rectangleMap.getWidth() - 1;
        switch (bullet.getDirection()) {
            case UP:
                collisionTime = (yMax - yBullet) / bullet.getSpeed();
                break;
            case DOWN:
                collisionTime = yBullet / bullet.getSpeed();
                break;
            case LEFT:
                collisionTime = xBullet / bullet.getSpeed();
                break;
            case RIGHT:
                collisionTime = (xMax - xBullet) / bullet.getSpeed();
                break;
            default:
                throw new IllegalStateException("unreachable");
        }
        if (collisionTime <= timeTick) {
            return Optional.of(collisionTime);
        } else {
            return Optional.empty();
        }
    }

    private static float endOfMoveTime(final Tank tank) {
        return tank.getMoveProgress() / tank.getMoveSpeed();
    }

    private static Optional<Float> collisionTimeWithUnitCircle(
            final FloatPoint2D point,
            final FloatPoint2D pointSpeed,
            final FloatPoint2D circleCenter,
            final FloatPoint2D circleSpeed,
            final float timeLimit
    ) {
        final float deltaX = circleCenter.getX() - point.getX();
        final float deltaY = circleCenter.getY() - point.getY();
        final float deltaXSpeed = circleSpeed.getX() - pointSpeed.getX();
        final float deltaYSpeed = circleSpeed.getY() - pointSpeed.getY();
        final float a = (float) (Math.pow(deltaXSpeed, 2.0) + Math.pow(deltaYSpeed, 2.0));
        final float b = 2 * (deltaXSpeed * deltaX + deltaYSpeed * deltaY);
        final float c = (float) (Math.pow(deltaX, 2.0) + Math.pow(deltaY, 2.0)) - 1.0f;
        return solveQuadraticEquation(a, b, c).flatMap(it -> chooseMinimalLimitedByTimeSolution(it, timeLimit));
    }

    private static Optional<Float> chooseMinimalLimitedByTimeSolution(final Solution solution, final float time) {
        if (solution.solutionOne >= 0 && solution.solutionOne <= time) {
            return Optional.of(solution.solutionOne);
        } else if (solution.solutionOne < 0 && solution.solutionTwo >= 0) {
            return Optional.of(0.0f);
        } else {
            return Optional.empty();
        }
    }

    /**
     * ax^2 + bx + c = 0; a is much greater than zero, due to bullet speed greater than tank
     */
    private static Optional<Solution> solveQuadraticEquation(final float a, final float b, final float c) {
        final float discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return Optional.empty();
        } else {
            return Optional.of(new Solution(
                            (-b - (float) Math.sqrt(discriminant)) / 2 * a,
                            (-b + (float) Math.sqrt(discriminant)) / 2 * a
                    )
            );
        }
    }

    private static class Solution {
        private final float solutionOne;
        private final float solutionTwo;

        Solution(final float solutionOne, final float solutionTwo) {
            this.solutionOne = solutionOne;
            this.solutionTwo = solutionTwo;
        }
    }

    private static class Hit {
        private final Bullet bullet;
        @Nullable
        private final Tank tank;
        private final float hitTime;

        Hit(final Bullet bullet, @Nullable final Tank tank, final float hitTime) {
            this.bullet = bullet;
            this.tank = tank;
            this.hitTime = hitTime;
        }

        void apply() {
            getTank().ifPresent(tank -> tank.reduceHealth(bullet.getDamage()));
            bullet.setDead();
        }

        float getHitTime() {
            return hitTime;
        }

        Optional<Tank> getTank() {
            return Optional.ofNullable(tank);
        }

        Bullet getBullet() {
            return bullet;
        }
    }
}
