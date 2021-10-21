package ru.mipt.bit.platformer.graphic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.logic.MoveView;
import ru.mipt.bit.platformer.logic.Point2D;

import static ru.mipt.bit.platformer.graphic.GdxGameUtils.drawTextureRegionUnscaled;

public class GraphicsObject implements Renderable {
    private final MoveView gameObjectMoveView;
    private final TileMovement tileMovement;
    private final TextureRegion textureRegion;
    private final Rectangle rectangle;

    private GraphicsObject(
            final MoveView moveView,
            final TileMovement tileMovement,
            final TextureRegion textureRegion,
            final Rectangle rectangle
    ) {
        this.gameObjectMoveView = moveView;
        this.tileMovement = tileMovement;
        this.textureRegion = textureRegion;
        this.rectangle = rectangle;
    }

    public static GraphicsObject create(
            final MoveView moveView,
            final TileMovement tileMovement,
            final TextureRegion textureRegion
    ) {
        return new GraphicsObject(
                moveView,
                tileMovement,
                textureRegion,
                tileMovement.moveRectangleAtTileCenter(
                        GdxGameUtils.createBoundingRectangle(textureRegion),
                        toGdxPoint(moveView.currentPosition())
                )
        );
    }

    public void render(final Batch runningBatch) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                toGdxPoint(gameObjectMoveView.currentPosition()),
                toGdxPoint(gameObjectMoveView.destinationPosition()),
                gameObjectMoveView.progress()
        );
        drawTextureRegionUnscaled(runningBatch, textureRegion, rectangle, gameObjectMoveView.angle());
    }

    private static GridPoint2 toGdxPoint(final Point2D point) {
        return new GridPoint2(point.getX(), point.getY());
    }
}
