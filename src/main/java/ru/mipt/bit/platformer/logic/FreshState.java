package ru.mipt.bit.platformer.logic;

import java.util.List;

/**
 * Domain entity.
 */
public class FreshState implements TankState {

    private final Tank tank;

    public FreshState(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void shoot(Level level) {
        var canon = tank.getCanon();
        var direction = tank.getTankMove().getDirection();
        canon.shoot(
                direction,
                tank.position().plus(FloatPoint2D.from(direction.unitVector()).multiply(0.5f))
        ).ifPresent(level::addBullet);
    }


    @Override
    public void update(float tick) {
        tank.getCanon().update(tick);
        tank.getTankMove().update(tick, moveSpeed());
    }

    @Override
    public void reduceHealth(int damage) {
        tank.setHealth(tank.getHealth() - damage);
        if (tank.getHealthPercent() < 60) {
            tank.setTankState(new MediumDamagedState(tank));
        }
    }

    @Override
    public void startMove(Direction direction, List<Colliding> collidings) {
        tank.getTankMove().startMove(direction, collidings);
    }

    @Override
    public float moveSpeed() {
        return tank.getNominalSpeed();
    }
}
