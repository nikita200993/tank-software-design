package ru.mipt.bit.platformer.logic;

/**
 * Domain entity.
 */
public enum Direction {
    UP {
        @Override
        public float getAngle() {
            return 90;
        }

        @Override
        public Point2D unitVector() {
            return up;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX(), position.getY() + 1);
        }
    },
    DOWN {
        @Override
        public float getAngle() {
            return -90;
        }

        @Override
        public Point2D unitVector() {
            return down;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX(), position.getY() - 1);
        }
    },
    LEFT {
        @Override
        public float getAngle() {
            return -180;
        }

        @Override
        public Point2D unitVector() {
            return left;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX() - 1, position.getY());
        }
    },
    RIGHT {
        @Override
        public float getAngle() {
            return 0;
        }

        @Override
        public Point2D unitVector() {
            return right;
        }

        @Override
        Point2D computeNextPosition(final Point2D position) {
            return new Point2D(position.getX() + 1, position.getY());
        }
    };

    private static final Point2D up = new Point2D(0, 1);
    private static final Point2D down = new Point2D(0, -1);
    private static final Point2D left = new Point2D(-1, 0);
    private static final Point2D right = new Point2D(1, 0);

    public abstract float getAngle();

    public abstract Point2D unitVector();

    abstract Point2D computeNextPosition(Point2D position);
}
