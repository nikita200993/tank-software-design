package ru.mipt.bit.platformer.logic;

public class ShootCommand implements Command {

    private final Tank tank;
    private final GameState gameState;

    public ShootCommand(final Tank tank, final GameState gameState) {
        this.tank = tank;
        this.gameState = gameState;
    }

    @Override
    public void execute() {
        gameState.shoot(tank);
    }
}
