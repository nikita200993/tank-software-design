package ru.mipt.bit.platformer.gdx.graphics;

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
