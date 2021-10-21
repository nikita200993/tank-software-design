package ru.mipt.bit.platformer.device;

import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.logic.Direction;

public class DefaultGdxDirectionResolver implements DirectionResolver {

    @Override
    public Optional<Direction> resolveDirection() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            return Optional.of(Direction.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            return Optional.of(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            return Optional.of(Direction.DOWN);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            return Optional.of(Direction.RIGHT);
        } else {
            return Optional.empty();
        }
    }
}
