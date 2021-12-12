package ru.mipt.bit.platformer.logic;

import java.util.List;
import java.util.Optional;

import ru.mipt.bit.platformer.logic.shoot.Bullet;

public class MediumDamagedState implements TankState {
    private final Tank tank;

    public MediumDamagedState(Tank tank) {
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
        if (tank.getHealthPercent() < 30) {
            tank.setTankState(new HeavyDamagedState(tank));
        }
    }

    @Override
    public void startMove(Direction direction, List<Colliding> collidings) {
        tank.getTankMove().startMove(direction, collidings);
    }

    @Override
    public float moveSpeed() {
        return tank.getNominalSpeed() / 2;
    }
}
