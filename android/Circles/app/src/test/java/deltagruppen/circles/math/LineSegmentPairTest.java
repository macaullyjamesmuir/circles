package deltagruppen.circles.math;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LineSegmentPairTest {

    @Test
    public void testGetIntersection() {

        LineSegmentPair pair;

        // Intersection at an endpoint
        pair = new LineSegmentPair(ls(p(0,0),p(0,1)), ls(p(0,1), p(1,1)));
        assertEquals(p(0,1), pair.getIntersection());

        // Intersection somewhere other than an endpoint
        pair = new LineSegmentPair(ls(p(0,1), p(2,1)),ls(p(1,0), p(1,2)));
        assertEquals(p(1,1), pair.getIntersection());

        // Same as before, but with one line defined in the opposite direction
        pair = new LineSegmentPair(ls(p(2,1), p(0,1)),ls(p(1,0), p(1,2)));
        assertEquals(p(1,1), pair.getIntersection());

        // Parallel and non-collinear
        pair = new LineSegmentPair(ls(p(0,0), p(0,1)),ls(p(1,0), p(1,1)));
        assertNull(pair.getIntersection());

        // Collinear but disjoint
        pair = new LineSegmentPair(ls(p(0,0), p(0,1)),ls(p(0,2), p(0,3)));
        assertNull(pair.getIntersection());

        // Overlapping (collinear and non-disjoint)
        pair = new LineSegmentPair(ls(p(0,0), p(0,2)),ls(p(0,1), p(0,3)));
        assertNotNull(pair.getIntersection());
        assertTrue(pair.getIntersection().equals(p(0,2)) || pair.getIntersection().equals(p(0,1)));

        // Overlapping, one segment contained within the other
        pair = new LineSegmentPair(ls(p(0,0), p(0,3)),ls(p(0,1), p(0,2)));
        assertNotNull(pair.getIntersection());
        assertTrue(pair.getIntersection().equals(p(0,1)) || pair.getIntersection().equals(p(0,2)));
    }

    /**
     * Helper method for creating points. Keeps the test code
     * short and sweet.
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @return Something that represents a point.
     */
    private Vector2D p(double x, double y)
    {
        return new Vector2D(x,y);
    }

    /**
     * Helper method for creating line segments. Keeps the test
     * code short and sweet.
     * @param p1 A point
     * @param p2 A point
     * @return The line segment that runs between p1 and p2.
     */
    private LineSegment ls(Vector2D p1, Vector2D p2)
    {
        return new LineSegment(p1,p2);
    }
}