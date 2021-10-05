package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.UserInput;

public class GameLogic {
    private final Player player;
    private final GridPoint2 obstacle;

    public GameLogic(final Player player, final GridPoint2 obstacle) {
        this.player = player;
        this.obstacle = obstacle;
    }

    public void update(final UserInput userInput, final float time) {
        player.updateProgress(time);
        userInput.getDirection()
                .ifPresent(direction -> player.startMove(direction, obstacle));
    }

    public Player getPlayer() {
        return player;
    }

    public GridPoint2 getObstacle() {
        return obstacle;
    }
}
