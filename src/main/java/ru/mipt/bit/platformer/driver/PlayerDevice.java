package ru.mipt.bit.platformer.driver;

import java.util.Optional;

import ru.mipt.bit.platformer.logic.Direction;

public interface PlayerDevice {
    Optional<Direction> getRequestedMoveDirection();
    boolean isShootRequested();
    boolean isHealthToggleRequested();
}
