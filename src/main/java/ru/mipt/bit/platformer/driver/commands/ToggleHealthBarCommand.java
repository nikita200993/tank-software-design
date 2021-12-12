package ru.mipt.bit.platformer.driver.commands;

import ru.mipt.bit.platformer.driver.UISettings;
import ru.mipt.bit.platformer.logic.Command;

/**
 * Use case.
 */
public class ToggleHealthBarCommand implements Command {
    private final UISettings uiSettings;

    public ToggleHealthBarCommand(UISettings uiSettings) {
        this.uiSettings = uiSettings;
    }

    @Override
    public void execute() {
        uiSettings.toggleHealth();
    }
}
