package ru.mipt.bit.platformer.logic;

import java.util.List;
import java.util.Optional;

import ru.mipt.bit.platformer.logic.shoot.Bullet;

interface TankState {
    Optional<Bullet> shoot();
    void update(float tick);
    void reduceHealth(int damage);
    void startMove(Direction direction, List<Colliding> collidings);
    float moveSpeed();
}
