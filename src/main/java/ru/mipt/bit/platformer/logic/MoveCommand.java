package ru.mipt.bit.platformer.logic;

import java.util.List;

import ru.mipt.bit.platformer.Command;

public class MoveCommand implements Command {
    private final Tank tank;
    private final Direction direction;
    private final List<Colliding> collidingObjects;

    public MoveCommand(final Tank tank, final Direction direction, final List<Colliding> collidingObjects) {
        this.tank = tank;
        this.direction = direction;
        this.collidingObjects = collidingObjects;
    }

    @Override
    public void execute() {
        tank.startMove(direction, collidingObjects);
    }
}
