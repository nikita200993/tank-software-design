package ru.mipt.bit.platformer.ai;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.awesome.ai.AI;
import org.awesome.ai.Recommendation;
import org.awesome.ai.state.immovable.Obstacle;
import org.awesome.ai.state.movable.Bot;
import org.awesome.ai.state.movable.Orientation;
import org.awesome.ai.state.movable.Player;
import ru.mipt.bit.platformer.logic.Colliding;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.Direction;
import ru.mipt.bit.platformer.logic.GameState;
import ru.mipt.bit.platformer.logic.MoveCommand;
import ru.mipt.bit.platformer.logic.Tank;

public class TankGameAIAdapter implements TankGameAI {

    private final AI ai;

    public TankGameAIAdapter(final AI ai) {
        this.ai = ai;
    }

    @Override
    public List<Command> computeAiCommands(final GameState gameState) {
        return ai.recommend(toLibraryGameState(gameState))
                .stream()
                .filter(recommendation -> recommendation.getActor().getSource() != gameState.getPlayer())
                .map(recommendation -> toCommand(recommendation, gameState.getCollidingObjects()))
                .collect(Collectors.toList());
    }

    private static org.awesome.ai.state.GameState toLibraryGameState(final GameState gameState) {
        return org.awesome.ai.state.GameState.builder()
                .levelHeight(gameState.getHeight())
                .levelWidth(gameState.getWidth())
                .bots(
                        gameState.getAiTanks().stream()
                                .map(TankGameAIAdapter::toBot)
                                .collect(Collectors.toList())
                )
                .player(toPlayer(gameState.getPlayer()))
                .obstacles(
                        gameState.getObstacles().stream()
                                .map(point -> new Obstacle(point.getX(), point.getY()))
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static Bot toBot(final Tank tank) {
        return Bot.builder()
                .source(tank)
                .destX(tank.destinationPosition().getX())
                .destY(tank.destinationPosition().getY())
                .x(tank.currentPosition().getX())
                .y(tank.currentPosition().getY())
                .orientation(toOrientation(tank.getDirection()))
                .build();
    }

    private static Player toPlayer(final Tank playerTank) {
        return Player.builder()
                .source(playerTank)
                .x(playerTank.currentPosition().getX())
                .y(playerTank.currentPosition().getY())
                .destX(playerTank.destinationPosition().getX())
                .destY(playerTank.destinationPosition().getY())
                .orientation(toOrientation(playerTank.getDirection()))
                .build();
    }

    private static Command toCommand(final Recommendation recommendation, final List<Colliding> collidingObjects) {
        final var tank = (Tank) recommendation.getActor().getSource();
        final var action = recommendation.getAction();
        final var moveCommandFactory = moveCommandFactory(tank, collidingObjects);
        switch (action) {
            case MoveNorth:
                return moveCommandFactory.apply(Direction.UP);
            case MoveEast:
                return moveCommandFactory.apply(Direction.RIGHT);
            case MoveSouth:
                return moveCommandFactory.apply(Direction.DOWN);
            case MoveWest:
                return moveCommandFactory.apply(Direction.LEFT);
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    private static Orientation toOrientation(final Direction direction) {
        switch (direction) {
            case UP:
                return Orientation.N;
            case DOWN:
                return Orientation.S;
            case LEFT:
                return Orientation.W;
            case RIGHT:
                return Orientation.E;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    private static Function<Direction, MoveCommand> moveCommandFactory(
            final Tank tank,
            final List<Colliding> collidingObjects
    ) {
        return direction -> new MoveCommand(tank, direction, collidingObjects);
    }
}
