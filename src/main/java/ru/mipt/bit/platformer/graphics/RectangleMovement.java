package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.logic.FloatPoint2D;

public class RectangleMovement {

    private final int tileWidth;
    private final int tileHeight;

    public RectangleMovement(final int tileWidth, final int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public Rectangle moveRectangle(final Rectangle rectangle, final FloatPoint2D floatPoint2D) {
        final float rectangleX = floatPoint2D.getX() * tileWidth + tileWidth / 2.0f - rectangle.getWidth() / 2.0f;
        final float rectangleY = floatPoint2D.getY() * tileHeight + tileHeight / 2.0f - rectangle.getHeight() / 2.0f;
        return rectangle.setX(rectangleX)
                .setY(rectangleY);
    }
}
