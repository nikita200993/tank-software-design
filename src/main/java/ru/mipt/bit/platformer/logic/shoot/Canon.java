package ru.mipt.bit.platformer.logic.shoot;

import java.util.Optional;

import ru.mipt.bit.platformer.logic.Direction;
import ru.mipt.bit.platformer.logic.FloatPoint2D;

public class Canon {
    private final float reloadTime;
    private final int damage;
    private final float bulletSpeed;
    private float reloadFinishTime;

    public Canon(final float reloadTime, final int damage, final float bulletSpeed) {
        this.reloadTime = reloadTime;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.reloadFinishTime = 0;
    }

    public Optional<Bullet> shoot(final Direction direction, final FloatPoint2D position) {
        if (this.reloadFinishTime <= 0) {
            System.out.println("shooting");
            reloadFinishTime = reloadTime;
            return Optional.of(new Bullet(bulletSpeed, damage, direction, position));
        } else {
            return Optional.empty();
        }
    }

    public void update(final float timeTick) {
        reloadFinishTime -= timeTick;
    }
}
