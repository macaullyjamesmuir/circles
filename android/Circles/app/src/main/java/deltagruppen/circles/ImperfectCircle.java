package deltagruppen.circles;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import deltagruppen.circles.math.LineSegment;

/**
 * Represents an imperfect circle drawn by the user.
 */
public class ImperfectCircle
{

    private final List<PointF> points;

    /**
     * Create a new imperfect circle from the given points.
     * Throws an IllegalArgumentException if the given set
     * of points can't be used.
     * @param input A list of points.
     */
    public ImperfectCircle(List<PointF> input)
    {
        // Generate a list of line segments from the input
        List<LineSegment> segments = new ArrayList<>();
        for (int i = 1; i < input.size(); i++) {
            segments.add(new LineSegment(input.get(i-1), input.get(i)));
        }

        // Find the first place two line segments intersect, and only
        // use the points that are on that closed curve.
        for (int i = 0; i < segments.size(); i++) {
            for (int j = i + 2; j < segments.size(); j++) {
                LineSegment s1 = segments.get(i);
                LineSegment s2 = segments.get(j);

                PointF intersection = s1.getIntersection(s2);
                if (intersection != null) {
                    points = new ArrayList<>(input.subList(i, j + 1));
                    points.add(intersection);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Can't create a circle from the given list.");
    }
}
