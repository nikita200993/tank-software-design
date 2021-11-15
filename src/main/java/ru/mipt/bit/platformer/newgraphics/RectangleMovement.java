package ru.mipt.bit.platformer.newgraphics;

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
        final var movedToCenter = moveToCenter(rectangle, floatPoint2D);
        final float remainderX = floatPoint2D.getX() - (int) floatPoint2D.getX();
        final float remainderY = floatPoint2D.getY() - (int) floatPoint2D.getY();
        return movedToCenter.setX(movedToCenter.getX() + tileWidth * remainderX)
                .setY(movedToCenter.getY() + tileHeight * remainderY);
    }

    private Rectangle moveToCenter(final Rectangle rectangle, final FloatPoint2D floatPoint2D) {
        final int logicalX = (int) floatPoint2D.getX();
        final int logicalY = (int) floatPoint2D.getY();
        final float rectangleX = logicalX * tileWidth + tileWidth / 2.0f - rectangle.getWidth() / 2.0f;
        final float rectangleY = logicalY * tileHeight + tileHeight / 2.0f - rectangle.getHeight() / 2.0f;
        return rectangle.setX(rectangleX)
                .setY(rectangleY);
    }
}
