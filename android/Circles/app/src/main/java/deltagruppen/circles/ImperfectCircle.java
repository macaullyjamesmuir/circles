package deltagruppen.circles;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import deltagruppen.circles.math.LineSegment;

/**
 * Represents an imperfect circle drawn by the user.
 */
public class ImperfectCircle {

    private final List<LineSegment> segments;

    /**
     * Create a new imperfect circle from the given points.
     * Throws an IllegalArgumentException if the given set
     * of points can't be used.
     * @param points A list of points.
     */
    public ImperfectCircle(List<PointF> points)
    {
        // Generate the line segments.
        List<LineSegment> segments = new ArrayList<>();
        for (int i = 1; i < points.size(); i++) {
            segments.add(new LineSegment(points.get(i - 1), points.get(i)));
        }

        // Try to find intersecting line segments.
        for (int i = 0; i < segments.size(); i++) {
            LineSegment segment = segments.get(i);
            for (int j = i + 2; j < segments.size(); j++) {
                if (segment.getIntersection(segments.get(j)) != null) {
                    this.segments = trim(segments, i, j);
                    return;
                }
            }
        }

        // If we get here, nothing to do but bail.
        throw new IllegalArgumentException("Not a circle");
    }

    /**
     * Remove all elements in the list before and after the
     * given start and end indexes (exclusive).
     * @param list A list
     * @param start A valid start index
     * @param end A valid end index
     * @return The same list, trimmed.
     */
    private List<LineSegment> trim(List<LineSegment> list, int start, int end)
    {
        for (int i = 0; i < start; i++)             list.remove(0);
        for (int i = list.size() - 1; i > end; i--) list.remove(list.size() - 1);
        return list;
    }
}
