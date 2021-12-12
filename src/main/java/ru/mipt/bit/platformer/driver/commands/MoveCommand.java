package ru.mipt.bit.platformer.driver.commands;

import java.util.List;

import ru.mipt.bit.platformer.logic.Colliding;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.Direction;
import ru.mipt.bit.platformer.logic.Tank;

/**
 * Use case.
 */
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
