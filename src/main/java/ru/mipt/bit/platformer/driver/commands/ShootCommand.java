package ru.mipt.bit.platformer.driver.commands;

import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Tank;

/**
 * Use case.
 */
public class ShootCommand implements Command {

    private final Tank tank;
    private final Level level;

    public ShootCommand(final Tank tank, final Level level) {
        this.tank = tank;
        this.level = level;
    }

    @Override
    public void execute() {
        tank.shoot(level);
    }
}
