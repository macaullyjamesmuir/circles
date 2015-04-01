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

                    // The intersection should be in the list of points
                    points.add(intersection);

                    // Add the first point to the end of the list
                    points.add(new PointF((float) s1.p1.getX(), (float) s1.p1.getY()));
                    return;
                }
                if (intersection == null) {
                    if(segmentsInProximity(s1,s2)) {
                        points = new ArrayList<>(input.subList(i, j + 1));
                        points.add(new PointF((float) s1.p1.getX(), (float) s1.p1.getY()));
                        return;
                    }
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

    /**
     * Check to se if the line segments are in accepted proximity of each other.
     * @param s1 First line segment
     * @param s2 Second line segment
     * @return True or false
     */
    public boolean segmentsInProximity(LineSegment s1, LineSegment s2) {

        float DMax = 10, D;
        double dX, dY;

        dX = s1.p1.getX() - s2.p2.getX();
        dY = s1.p1.getY() - s2.p2.getY();

        D = (float) Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

        if (D < DMax) {
            return true;
        }
        return false;
    }
}
