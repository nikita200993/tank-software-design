package ru.mipt.bit.platformer.logic;

import java.util.List;

import ru.mipt.bit.platformer.UserInput;

public class GameLogic {
    private final Player player;
    private final List<Point2D> obstacles;

    public GameLogic(final Player player, List<Point2D> obstacles) {
        this.player = player;
        this.obstacles = obstacles;
    }

    public void update(final UserInput userInput, final float time) {
        player.updateProgress(time);
        userInput.getDirection()
                .ifPresent(direction -> player.startMove(direction, obstacles));
    }

    public Player getPlayer() {
        return player;
    }

    public List<Point2D> getObstacles() {
        return obstacles;
    }
}
