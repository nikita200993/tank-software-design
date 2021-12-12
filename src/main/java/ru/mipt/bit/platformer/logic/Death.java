package ru.mipt.bit.platformer.logic;

import java.util.Optional;
import javax.annotation.Nullable;

import ru.mipt.bit.platformer.logic.shoot.Bullet;

public class Death {
    private final Bullet bullet;
    @Nullable
    private final Tank tank;
    private final float deathTime;

    public Death(final Bullet bullet, @Nullable final Tank tank, final float deathTime) {
        this.bullet = bullet;
        this.tank = tank;
        this.deathTime = deathTime;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public Optional<Tank> getTank() {
        return Optional.ofNullable(tank);
    }

    public float getDeathTime() {
        return deathTime;
    }
}
