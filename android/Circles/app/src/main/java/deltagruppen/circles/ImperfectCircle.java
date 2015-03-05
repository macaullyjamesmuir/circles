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

                    // The intersection should be in the list of points
                    points.add(intersection);

                    // Add the first point to the end of the list
                    points.add(new PointF((float) s1.p1.getX(), (float) s1.p1.getY()));
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

        PointF p1, p2;
        for (int i = 1; i < points.size(); i++) {
            p1 = points.get(i-1);
            p2 = points.get(i);
            length += Math.sqrt(
                (p2.x - p1.x) * (p2.x - p1.x) +
                (p2.y - p1.y) * (p2.y - p1.y)
            );
        }

        return length;
    }

    /**
     * Get the signed area of the imperfect circle.
     * @return The area.
     */
    public double getArea()
    {
        double area = 0;

        PointF p1, p2;
        for (int i = 1; i < points.size(); i++) {
            p1 = points.get(i-1);
            p2 = points.get(i);
            area += 0.5 * (p2.x + p1.x)*(p2.y - p1.y);
        }

        return area;
    }

    /**
     * Get the centroid of the imperfect circle. The algorithm used is
     * described at http://en.wikipedia.org/wiki/Centroid#Centroid_of_polygon
     * @return The centroid.
     */
    public PointF getCentroid()
    {
        float x     = 0;
        float y     = 0;
        double area = getArea();

        PointF p1, p2;
        for (int i = 1; i < points.size(); i++) {
            p1 = points.get(i-1);
            p2 = points.get(i);
            x += (p1.x + p2.x) * (p1.x * p2.y - p2.x * p1.y);
            y += (p1.y + p2.y) * (p1.x * p2.y - p2.x * p1.y);
        }

        x = x / (6 * (float) area);
        y = y / (6 * (float) area);

        return new PointF(x, y);
    }
}
