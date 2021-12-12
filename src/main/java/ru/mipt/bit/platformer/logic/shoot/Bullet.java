package ru.mipt.bit.platformer.logic.shoot;

import ru.mipt.bit.platformer.logic.Direction;
import ru.mipt.bit.platformer.logic.FloatPoint2D;
import ru.mipt.bit.platformer.logic.GameObjectView;

/**
 * Domain entity.
 */
public class Bullet implements GameObjectView {
    private final float speed;
    private final int damage;
    private final Direction direction;
    private FloatPoint2D position;


    public Bullet(final float speed, final int damage, final Direction direction, final FloatPoint2D position) {
        this.speed = speed;
        this.damage = damage;
        this.direction = direction;
        this.position = position;
    }

    @Override
    public FloatPoint2D position() {
        return position;
    }

    @Override
    public float angle() {
        return direction.getAngle();
    }

    public int getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }

    public void update(final float time) {
        position = position.plus(FloatPoint2D.from(direction.unitVector()).multiply(time * speed));
    }

    public Direction getDirection() {
        return direction;
    }
}
