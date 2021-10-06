package ru.mipt.bit.platformer;

import java.util.Optional;
import javax.annotation.Nullable;

import ru.mipt.bit.platformer.logic.Direction;

public class UserInput {
    @Nullable
    private final Direction direction;

    public UserInput(@Nullable final Direction direction) {
        this.direction = direction;
    }

    public Optional<Direction> getDirection() {
        return Optional.ofNullable(direction);
    }
}
