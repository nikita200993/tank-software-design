package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import static ru.mipt.bit.platformer.util.GdxGameUtils.decrementedX;
import static ru.mipt.bit.platformer.util.GdxGameUtils.decrementedY;
import static ru.mipt.bit.platformer.util.GdxGameUtils.incrementedX;
import static ru.mipt.bit.platformer.util.GdxGameUtils.incrementedY;

enum Direction {
    UP {
        @Override
        float getRotation() {
            return 90;
        }

        @Override
        GridPoint2 computeNextPosition(final GridPoint2 position) {
            return incrementedY(position);
        }
    },
    DOWN {
        @Override
        float getRotation() {
            return -90;
        }

        @Override
        GridPoint2 computeNextPosition(final GridPoint2 position) {
            return decrementedY(position);
        }
    },
    LEFT {
        @Override
        float getRotation() {
            return -180;
        }

        @Override
        GridPoint2 computeNextPosition(final GridPoint2 position) {
            return decrementedX(position);
        }
    },
    RIGHT {
        @Override
        float getRotation() {
            return 0;
        }

        @Override
        GridPoint2 computeNextPosition(final GridPoint2 position) {
            return incrementedX(position);
        }
    };

    abstract float getRotation();

    abstract GridPoint2 computeNextPosition(GridPoint2 position);
}
