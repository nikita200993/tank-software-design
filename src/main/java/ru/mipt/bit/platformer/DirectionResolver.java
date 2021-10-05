package ru.mipt.bit.platformer;

import java.util.Optional;

import ru.mipt.bit.platformer.logic.Direction;

public interface DirectionResolver {
    Optional<Direction> resolveDirection();
}
