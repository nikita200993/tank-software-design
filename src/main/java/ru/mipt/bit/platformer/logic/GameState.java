package ru.mipt.bit.platformer.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class GameState {
    private final Tank player;
    private final List<Tank> aiTanks;
    private final List<Colliding> obstacles;
    private final Random random;

    public GameState(final Tank player, List<Tank> aiTanks, final List<Colliding> obstacles) {
        this.player = player;
        this.aiTanks = aiTanks;
        this.obstacles = obstacles;
        this.random = new Random();
    }

    public static GameState create(final Level level) {
        final var player = new Tank(level.getPlayerCoordinate());
        final var aiTanks = level.getAiPlayers()
                .stream()
                .map(Tank::new)
                .collect(Collectors.toList());
        final var obstacles = new ArrayList<Colliding>(aiTanks);
        obstacles.add(player);
        level.getTreesCoordinates()
                .stream()
                .map(SinglePoint::new)
                .forEach(obstacles::add);
        obstacles.add(new RectangleMap(level.getWidth(), level.getHeight()));
        return new GameState(player, aiTanks, obstacles);
    }

    public List<MoveView> getMoveViews() {
        final var moveViews = new ArrayList<MoveView>(aiTanks);
        moveViews.add(player);
        return moveViews;
    }

    public void update(final UserInput userInput, final float time) {
        player.updateProgress(time);
        aiTanks.forEach(tank -> tank.updateProgress(time));
        userInput.getDirection()
                .ifPresent(direction -> player.startMove(direction, obstacles));
        for (final var aiTank : aiTanks) {
            getRandomDirectionOrNothing()
                    .ifPresent(direction -> aiTank.startMove(direction, obstacles));
        }
    }

    private Optional<Direction> getRandomDirectionOrNothing() {
        final var directions = Direction.values();
        final int randomInt = random.nextInt(directions.length + 1);
        if (randomInt == directions.length) {
            return Optional.empty();
        } else {
            return Optional.of(directions[randomInt]);
        }
    }
}
