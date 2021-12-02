package ru.mipt.bit.platformer.graphics;

public class UISettings {

    private boolean toggleHealth;

    public UISettings() {
        this.toggleHealth = false;
    }

    public void toggleHealth() {
        toggleHealth = !toggleHealth;
    }

    public boolean isToggleHealth() {
        return toggleHealth;
    }
}
