package ru.mipt.bit.platformer.graphic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class GraphicsSimpleObject implements AutoCloseable {
    private final TileMovement tileMovement;
    private final GridPoint2 current;
    private final GridPoint2 destination;
    private final Texture texture;
    private final TextureRegion textureRegion;
    private final Rectangle rectangle;
    private float rotation;
    private float moveProgress;

    public GraphicsSimpleObject(
            final TileMovement tileMovement,
            final GridPoint2 current,
            final GridPoint2 destination,
            final Texture texture,
            final TextureRegion textureRegion,
            final float rotation,
            final float moveProgress
    ) {
        this.tileMovement = tileMovement;
        this.current = current;
        this.destination = destination;
        this.texture = texture;
        this.rectangle = tileMovement.moveRectangleBetweenTileCenters(
                GdxGameUtils.createBoundingRectangle(textureRegion),
                current,
                destination,
                moveProgress
        );
        this.textureRegion = textureRegion;
        this.rotation = rotation;
        this.moveProgress = moveProgress;
    }

    public GraphicsSimpleObject withCurrent(final GridPoint2 current) {
        this.current.set(current);
        return this;
    }

    public GraphicsSimpleObject withDestination(final GridPoint2 destination) {
        this.destination.set(destination);
        return this;
    }

    public GraphicsSimpleObject withRotation(final float rotation) {
        this.rotation = rotation;
        return this;
    }

    public GraphicsSimpleObject withMoveProgress(final float moveProgress) {
        this.moveProgress = moveProgress;
        return this;
    }

    public void render(final Batch currentBatch) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, current, destination, moveProgress);
        drawTextureRegionUnscaled(currentBatch, textureRegion, rectangle, rotation);
    }

    @Override
    public void close() {
        texture.dispose();
    }
}
