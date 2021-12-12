package ru.mipt.bit.platformer.logic;

import java.util.List;

/**
 * Domain entity.
 */
public class HeavyDamagedState implements TankState {

    private final Tank tank;

    public HeavyDamagedState(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void shoot(Level level) {
    }

    @Override
    public void update(float tick) {
        tank.getCanon().update(tick);
        tank.getTankMove().update(tick, moveSpeed());
    }

    @Override
    public void reduceHealth(int damage) {
        tank.setHealth(tank.getHealth() - damage);
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
