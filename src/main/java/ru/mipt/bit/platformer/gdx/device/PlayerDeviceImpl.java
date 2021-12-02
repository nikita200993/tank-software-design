package ru.mipt.bit.platformer.gdx.device;

import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.driver.PlayerDevice;
import ru.mipt.bit.platformer.logic.Direction;

public class PlayerDeviceImpl implements PlayerDevice {

    @Override
    public Optional<Direction> getRequestedMoveDirection() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            return Optional.of(Direction.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            return Optional.of(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            return Optional.of(Direction.DOWN);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            return Optional.of(Direction.RIGHT);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean isShootRequested() {
        return Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    @Override
    public boolean isHealthToggleRequested() {
        return Gdx.input.isKeyJustPressed(Input.Keys.L);
    }
}
