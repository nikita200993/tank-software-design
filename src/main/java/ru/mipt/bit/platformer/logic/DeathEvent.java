package ru.mipt.bit.platformer.logic;

public class DeathEvent implements GameEvent<GameObjectView> {

    private final GameObjectView source;

    public DeathEvent(GameObjectView source) {
        this.source = source;
    }

    @Override
    public GameObjectView source() {
        return source;
    }
}
