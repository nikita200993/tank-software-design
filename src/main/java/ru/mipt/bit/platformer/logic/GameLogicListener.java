package ru.mipt.bit.platformer.logic;

import java.util.List;

public interface GameLogicListener {
    default void onRegister(
            List<? extends Tank> tanks,
            List<? extends GameObjectView> trees,
            List<? extends GameObjectView> bullets
    ) {
    }

    default void onBulletsDeath(final List<? extends GameObjectView> bullets) {
    }

    default void onTanksDeath(final List<? extends GameObjectView> tank) {
    }

    default void onBulletCreated(GameObjectView bullet) {
    }
}
