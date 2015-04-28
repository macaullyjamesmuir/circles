package perfectpi.circles.math;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a pair of line segments (yes, really!).
 * Used for calculating the intersection of two line segments.
 */
public class LineSegmentPair {
    private final LineSegment l1;
    private final LineSegment l2;

    /**
     * Create a pair of line segments from the given line segments.
     * @param l1 A line segment.
     * @param l2 A line segment.
     */
    public LineSegmentPair(LineSegment l1, LineSegment l2) {
        this.l1 = l1;
        this.l2 = l2;
    }

    /**
     * Get the intersection point of the two line segments.
     * The algorithm designed to do this is designed in detail
     * at http://stackoverflow.com/a/565282, though it's just
     * basic linear algebra really.
     * @return If the intersection point is uniquely defined, return
     *         that point. If the lines overlap, return an endpoint
     *         that lies in the overlap. Else return null.
     */
    public Vector2D getIntersection()
    {
        Vector2D p = l1.p1;
        Vector2D r = l1.p2.subtract(l1.p1);
        Vector2D q = l2.p1;
        Vector2D s = l2.p2.subtract(l2.p1);

        if (cross(r,s) != 0 )               return onePointIntersection(p, r, q, s);
        if (cross((q.subtract(p)), r) == 0) return overlapIntersection(p, r, q, s);
        return null;
    }

    /**
     * Suppose that there exists a unique intersection between l1 and
     * l2, and try to figure out what it is.
     * @return The intersection point, if one is found. Else null.
     */
    private Vector2D onePointIntersection(Vector2D p, Vector2D r, Vector2D q, Vector2D s)
    {
        // Calculate t and u so that p + tr = q + us.
        double t = cross(q.subtract(p), s) / cross(r,s);
        double u = cross(q.subtract(p), r) / cross(r,s);

        // Make sure the intersection is on the line segments (not the extended lines)
        if (t < 0 || u < 0 || t > 1 || u > 1) return null;

        // Return the intersection point
        return p.add(r.scalarMultiply(t));
    }

    /**
     * Assume that l1 and l2 are collinear, and try to figure out if
     * they overlap.
     * @return An endpoint of l1 or l2 that lies in the overlap.
     */
    private Vector2D overlapIntersection(Vector2D p, Vector2D r, Vector2D q, Vector2D s)
    {
        // Calculate the distance between the endpoints of the segments.
        double dotR = q.subtract(p).dotProduct(r);
        double dotS = p.subtract(q).dotProduct(s);

        // If the lines overlap, return an endpoint that lies in the overlap.
        if (dotR > 0 && dotR <= r.dotProduct(r)) return q;
        if (dotS > 0 && dotS <= s.dotProduct(s)) return p;

        // If not, the lines don't overlap so return null.
        return null;
    }

    /**
     * Returns the area of the parallelogram generate from the
     * endpoints of v1 and v2.
     * @param v1 A vector
     * @param v2 A vector
     * @return The (posssibly negative) area v1 and v2 generate.
     */
    private double cross(Vector2D v1, Vector2D v2) {
        return v1.getX() * v2.getY() - v2.getX() * v1.getY();
    }
}
