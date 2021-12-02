package ru.mipt.bit.platformer.logic;

import java.util.List;
import java.util.Optional;

import ru.mipt.bit.platformer.logic.shoot.Bullet;

/**
 * Domain entity.
 */
public class FreshState implements TankState {

    private final Tank tank;

    public FreshState(Tank tank) {
        this.tank = tank;
    }

    @Override
    public Optional<Bullet> shoot() {
        var canon = tank.getCanon();
        var direction = tank.getTankMove().getDirection();
        return canon.shoot(
                direction,
                tank.position().plus(FloatPoint2D.from(direction.unitVector()).multiply(0.5f))
        );
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
