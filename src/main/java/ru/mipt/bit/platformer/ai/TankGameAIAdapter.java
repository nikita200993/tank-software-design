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
import ru.mipt.bit.platformer.driver.MoveCommand;
import ru.mipt.bit.platformer.driver.ShootCommand;
import ru.mipt.bit.platformer.logic.Colliding;
import ru.mipt.bit.platformer.logic.Command;
import ru.mipt.bit.platformer.logic.Direction;
import ru.mipt.bit.platformer.logic.Level;
import ru.mipt.bit.platformer.logic.Tank;

/**
 * Output adapter.
 */
public class TankGameAIAdapter implements TankGameAI {

    private final AI ai;

    public TankGameAIAdapter(final AI ai) {
        this.ai = ai;
    }

    @Override
    public List<Command> computeAiCommands(final Level level) {
        return ai.recommend(toLibraryGameState(level))
                .stream()
                .filter(recommendation -> recommendation.getActor().getSource() != level.getPlayer())
                .map(recommendation -> toCommand(recommendation, level))
                .collect(Collectors.toList());
    }

    private static org.awesome.ai.state.GameState toLibraryGameState(final Level level) {
        return org.awesome.ai.state.GameState.builder()
                .levelHeight(level.getHeight())
                .levelWidth(level.getWidth())
                .bots(
                        level.getAiTanks().stream()
                                .map(TankGameAIAdapter::toBot)
                                .collect(Collectors.toList())
                )
                .player(toPlayer(level.getPlayer()))
                .obstacles(
                        level.getObstacles().stream()
                                .map(ru.mipt.bit.platformer.logic.Obstacle::getPosition)
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

    private static Command toCommand(final Recommendation recommendation, final Level level) {
        final var tank = (Tank) recommendation.getActor().getSource();
        final var action = recommendation.getAction();
        final var moveCommandFactory = moveCommandFactory(
                tank,
                level.getCollidingObjects()
        );
        switch (action) {
            case MoveNorth:
                return moveCommandFactory.apply(Direction.UP);
            case MoveEast:
                return moveCommandFactory.apply(Direction.RIGHT);
            case MoveSouth:
                return moveCommandFactory.apply(Direction.DOWN);
            case MoveWest:
                return moveCommandFactory.apply(Direction.LEFT);
            case Shoot:
                return new ShootCommand(tank, level);
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
