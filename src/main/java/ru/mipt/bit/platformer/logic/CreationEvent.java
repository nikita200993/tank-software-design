package ru.mipt.bit.platformer.logic;

public class CreationEvent implements GameEvent<GameObjectView> {

    private final GameObjectView source;

    public CreationEvent(GameObjectView source) {
        this.source = source;
    }

    @Override
    public GameObjectView source() {
        return source;
    }
}
