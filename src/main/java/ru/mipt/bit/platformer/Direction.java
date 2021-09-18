package ru.mipt.bit.platformer;

import static com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.Gdx;

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE;

    static Direction getDirection() {
        if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
            return Direction.UP;
        } else if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            return Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
            return Direction.DOWN;
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            return Direction.RIGHT;
        } else {
            return Direction.NONE;
        }
    }
}
