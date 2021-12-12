package ru.mipt.bit.platformer.driver.controllers;

import java.util.ArrayList;
import java.util.List;

import ru.mipt.bit.platformer.driver.PlayerDevice;
import ru.mipt.bit.platformer.driver.TankController;
import ru.mipt.bit.platformer.driver.UISettings;
import ru.mipt.bit.platformer.driver.commands.MoveCommand;
import ru.mipt.bit.platformer.driver.commands.ShootCommand;
import ru.mipt.bit.platformer.driver.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.Direction;
import ru.mipt.bit.platformer.logic.Level;

public class PlayerController implements TankController {

    private final PlayerDevice playerDevice;
    private final Level level;
    private final UISettings uiSettings;

    public PlayerController(PlayerDevice playerDevice, Level level, UISettings uiSettings) {
        this.playerDevice = playerDevice;
        this.level = level;
        this.uiSettings = uiSettings;
    }

    @Override
    public List<Command> getRequestedCommands() {
        var result = new ArrayList<Command>();
        playerDevice.getRequestedMoveDirection().ifPresent(
                direction -> result.add(createMoveCommand(direction))
        );
        if (playerDevice.isShootRequested()) {
            result.add(new ShootCommand(level.getPlayer(), level));
        }
        if (playerDevice.isHealthToggleRequested()) {
            result.add(new ToggleHealthBarCommand(uiSettings));
        }
        return result;
    }

    private MoveCommand createMoveCommand(Direction direction) {
        return new MoveCommand(level.getPlayer(), direction, level.getCollidingObjects());
    }
}
