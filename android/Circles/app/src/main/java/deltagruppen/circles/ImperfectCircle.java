package deltagruppen.circles;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
        // Clean the input (fixes issue #19)
        cleanInput(input);

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

    /**
     * Get the points that make up this imperfect circle.
     * @return A list of points.
     */
    public List<PointF> getPoints()
    {
        return points;
    }

    /**
     * Get the perimeter length of the imperfect circle.
     * @return The length of the perimeter.
     */
    public double getPerimeterLength()
    {
        double length = 0;

        // Go through all the points and add the length between them
        PointF p1, p2;
        for (int i = 1; i < points.size(); i++) {
            p1 = points.get(i-1);
            p2 = points.get(i);
            length += Math.sqrt(
                (p2.x - p1.x) * (p2.x - p1.x) +
                (p2.y - p1.y) * (p2.y - p1.y)
            );
        }

        // Don't forget to add the length between the first and last element.
        p1 = points.get(points.size() - 1);
        p2 = points.get(0);
        length += Math.sqrt(
            (p2.x - p1.x) * (p2.x - p1.x) +
            (p2.y - p1.y) * (p2.y - p1.y)
        );

        return length;
    }

    /**
     * Get the area of the imperfect circle.
     * @return The area.
     */
    public double getArea()
    {
        double area = 0;

        // Go through all the points and add the area they generate.
        PointF p1, p2;
        for (int i = 1; i < points.size(); i++) {
            p1 = points.get(i-1);
            p2 = points.get(i);
            area += 0.5 * (p2.x + p1.x)*(p2.y - p1.y);
        }

        // Don't forget to add the area the first and last element generate.
        p1 = points.get(points.size() - 1);
        p2 = points.get(0);
        area += 0.5 * (p2.x + p1.x)*(p2.y - p1.y);

        return Math.abs(area);
    }

    /**
     * Some devices seem to register the same point twice in a
     * row on touch move events, which messes with our algorithms
     * because we get line segments of zero length. This method
     * goes through the list and removes such duplicate points.
     *
     * See issue #19 (https://github.com/deltagruppen/circles/issues/19)
     * for more info.
     *
     * @param input A list of points.
     */
    private void cleanInput(List<PointF> input)
    {
        PointF p1, p2;
        ListIterator<PointF> iterator = input.listIterator();
        while (iterator.nextIndex() < input.size() - 2) {
            p1 = iterator.next();
            p2 = iterator.next();

            if (p1.equals(p2.x, p2.y)) iterator.remove();
            iterator.previous();
        }
    }
}
