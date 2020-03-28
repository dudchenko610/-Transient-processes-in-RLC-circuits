package com.crazydev.funnycircuits.math;

public class OverlapTester {

    public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {

        return  r1.corners[0].x < r2.corners[1].x &&
                r2.corners[0].x < r1.corners[1].x &&
                r1.corners[0].y < r2.corners[3].y &&
                r2.corners[0].y < r1.corners[3].y;

    }

    public static boolean pointInRectangle(Rectangle r, Vector2D p) {
        return r.corners[0].x <= p.x && r.corners[1].x >= p.x &&
                r.corners[0].y <= p.y && r.corners[3].y >= p.y;
    }

    public static Rectangle getOverlap(Rectangle r1, Rectangle r2) {

        if (overlapRectangles(r1, r2)) {
            if (r2.corners[3].y > r1.corners[3].y)  {
                r2.corners[2].set(r1.corners[2]);
                r2.corners[3].set(r1.corners[3]);
            }

            return r2;
        }

        return null;
    }

}
