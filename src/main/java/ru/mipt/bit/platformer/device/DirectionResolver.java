package ru.mipt.bit.platformer.device;

import java.util.Optional;

import ru.mipt.bit.platformer.logic.Direction;

public interface DirectionResolver {
    Optional<Direction> resolveDirection();
}
