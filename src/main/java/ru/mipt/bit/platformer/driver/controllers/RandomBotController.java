package ru.mipt.bit.platformer.driver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import ru.mipt.bit.platformer.driver.TankController;
import ru.mipt.bit.platformer.driver.commands.MoveCommand;
import ru.mipt.bit.platformer.driver.commands.ShootCommand;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.Direction;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Tank;

public class RandomBotController implements TankController {
    private final Level level;
    private final Tank tank;
    private final Random random;

    public RandomBotController(Level level, Tank tank, Random random) {
        this.level = level;
        this.tank = tank;
        this.random = random;
    }

    @Override
    public List<Command> getRequestedCommands() {
        var result = new ArrayList<Command>();
        getRandomDirection().ifPresent(direction -> result.add(createMoveCommand(direction)));
        if (shouldShoot()) {
            result.add(new ShootCommand(tank, level));
        }
        return result;
    }

    private Optional<Direction> getRandomDirection() {
        var directionNumber = random.nextInt(5);
        if (directionNumber == 0) {
            return Optional.empty();
        } else {
            return Optional.of(Direction.values()[directionNumber - 1]);
        }
    }

    private boolean shouldShoot() {
        return random.nextInt(2) == 1;
    }

    private MoveCommand createMoveCommand(Direction direction) {
        return new MoveCommand(tank, direction, level.getCollidingObjects());
    }
}
