package deltagruppen.circles.math;

import android.graphics.PointF;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * A simple wrapper class that represents a line segment.
 */
public class LineSegment {
    public final Vector2D p1;
    public final Vector2D p2;

    /**
     * Create a new line segment with the given points.
     * @param p1 A point.
     * @param p2 A point.
     */
    public LineSegment(PointF p1, PointF p2)
    {
        this(new Vector2D(p1.x, p1.y), new Vector2D(p2.x, p2.y));
    }

    /**
     * Create a new line segment with the given points.
     * @param p1 A point.
     * @param p2 A point.
     */
    public LineSegment(Vector2D p1, Vector2D p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Get the intersection of this line and the given segment.
     * @param segment A line segment.
     * @return If the intersection point is uniquely defined, return
     *         that point. If the lines overlap, return an endpoint
     *         that lies in the overlap. Else return null.
     */
    public PointF getIntersection(LineSegment segment)
    {
        Vector2D intersection = (new LineSegmentPair(this, segment)).getIntersection();
        if (intersection == null) {
            return null;
        }
        else {
            return new PointF((float) intersection.getX(), (float) intersection.getY());
        }
    }
}
