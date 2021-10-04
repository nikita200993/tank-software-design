package ru.mipt.bit.platformer;

import java.util.Optional;

public interface DirectionResolver {
    Optional<Direction> resolveDirection();
}
