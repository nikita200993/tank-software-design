package ru.mipt.bit.platformer.gdx.graphics;

/**
 * Domain entity.
 */
public interface Renderable {
    default void render() {
    }

    default void renderShape() {
    }
}
