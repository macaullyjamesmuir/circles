package deltagruppen.circles;

import android.graphics.PointF;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

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
    private static double      DMax = 10;



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

        // Insert and remove points so that there
        // is an even distance between all points.
        normalizeInput(input);

        // Generate a list of line segments from the input
        List<LineSegment> segments = new ArrayList<>();
        for (int i = 1; i < input.size(); i++) {
            segments.add(new LineSegment(input.get(i-1), input.get(i)));
        }

        // Find the first place two line segments intersect or two points are
        // in proximity of each other, and only use the points that are on
        // that closed curve.
        for (int i = 0; i < segments.size(); i++) {
            for (int j = i + 2; j < segments.size(); j++) {
                LineSegment s1 = segments.get(i);
                LineSegment s2 = segments.get(j);

                PointF intersection = s1.getIntersection(s2);
                int k = pointsInProximity(s1, s2);

                if (intersection != null) {
                    points = new ArrayList<>(input.subList(i, j + 1));

                    // The intersection should be in the list of points
                    points.add(intersection);

                    // Add the first point to the end of the list
                    points.add(new PointF((float) s1.p1.getX(), (float) s1.p1.getY()));
                    return;

                    // If two points are in proximity of each other all the points in
                    // between should be used for calculations. If k = 0 it means
                    // that the points are not in proximity.
                } else if (k != 0 && j > i + 10) {
                    switch (k) {

                        // In case 1 the first and the last points are closest
                        // to each other.
                        case 1:
                            points = new ArrayList<>(input.subList(i, j + 1));
                            points.add(new PointF((float) s1.p1.getX(), (float) s1.p1.getY()));
                            return;

                        // In case 2 the second and the last point are closest to each other.
                        case 2:
                            points = new ArrayList<>(input.subList(i + 1 , j + 1));
                            points.add(new PointF((float) s1.p2.getX(), (float) s1.p2.getY()));
                            return;

                        // In case 3 the second and the point before the last are closest.
                        case 3:
                            points = new ArrayList<>(input.subList(i + 1, j));
                            points.add(new PointF((float) s1.p2.getX(), (float) s1.p2.getY()));
                            return;

                        // In case 4 the first and the point before the last are closest.
                        case 4:
                            points = new ArrayList<>(input.subList(i, j));
                            points.add(new PointF((float) s1.p1.getX(), (float) s1.p1.getY()));
                            return;
                    }
                }
            }
        }

        points = new ArrayList<>(input.subList(0,input.size()));
        points.add(input.get(0));
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
     * Even out the distance between all points in a given list of points.
     * A point which is to close to the last point will be removed.
     * If the distance between two points is too great new points will
     * be inserted in the list.
     * @param input The given list of points
     */
    public void normalizeInput(List<PointF> input)
    {
        PointF p1, p2;
        int i = 0;

        // Will keep track of how many points that are
        // inserted.
        int pointsInserted;

        while (i < input.size() - 2) {

            p1 = input.get(i);
            p2 = input.get(++i);

            double D = distanceBetweenPoints(p1, p2);

            // No points should be closer than DMax to each other
            if (D <= DMax) {
                input.remove(i);
                i--;
            } else if (D > (DMax + (DMax / 2))) {

                // Insert points in the appropriate place and return the amount
                // of points added.
                pointsInserted = insertPointsBetween(p1, p2, input, D, i);
                i += pointsInserted;
            } else i++;
        }
    }

    /**
     * Find the distance between two points
     * @param p1 First point
     * @param p2 Second point
     * @return Distance
     */
    public double distanceBetweenPoints(Vector2D p1, Vector2D p2)
    {
        double dX, dY, D;

        dX = p1.getX() - p2.getX();
        dY = p1.getY() - p2.getY();

        D = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

        return D;
    }

    /**
     * Find the distance between two points
     * @param p1 First point
     * @param p2 Second point
     * @return Distance
     */
    public double distanceBetweenPoints(PointF p1, PointF p2)
    {
        double dX, dY, D;

        dX = p1.x - p2.x;
        dY = p1.y - p2.y;

        D = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

        return D;
    }

    /**
     * Check to se if the line segments are in accepted proximity of each other and find
     * the smallest difference between the end points.
     * @param s1 First line segment
     * @param s2 Second line segment
     * @return True or false
     */
    public int pointsInProximity(LineSegment s1, LineSegment s2)
    {
        int _case = 1;
        double dMin = distanceBetweenPoints(s1.p1, s2.p2);
        double distanceBetween_s1p2_s2p2 = distanceBetweenPoints(s1.p2, s2.p2);
        double distanceBetween_s1p2_s2p1 = distanceBetweenPoints(s1.p2, s2.p1);
        double distanceBetween_s1p1_s2p1 = distanceBetweenPoints(s1.p1, s2.p1);

        // Find the smallest difference in all situations between end points of two line segments.
        if (distanceBetween_s1p2_s2p2 < dMin) dMin = distanceBetween_s1p2_s2p2; _case = 2;
        if (distanceBetween_s1p2_s2p1 < dMin) dMin = distanceBetween_s1p2_s2p1; _case = 3;
        if (distanceBetween_s1p1_s2p1 < dMin) _case = 4;

        if (dMin < DMax) return _case;
        else return 0;
    }

    /**
     * Inserts points with an equal distance from each other on a straight line between
     * two other points in a list. Used when a line segment becomes very long to prevent
     * it from affecting the calculations when calculating pi negatively.
     *
     * @param p1 First point
     * @param p2 Second point
     * @param input Input list of PointF
     * @param D The distance between the two given points
     * @param startIndex The index in the list of the first point
     * @return The amount of points added to the list
     */
    public int insertPointsBetween(PointF p1, PointF p2, List<PointF> input, double D, int startIndex)
    {
        double dX, dY, currentX, currentY;

        // The amount of points to add between the first and the second.
        int i = pointsToAdd(D);
        int pointsAdded = i;

        // Distance between each new point
        dX = (p1.x - p2.x) / (i + 1);
        dY = (p1.y - p2.y) / (i + 1);

        // The point to add, will be updated after each point is added.
        currentX = p1.x;
        currentY = p1.y;

        while (i > 0) {
            currentX -= dX;
            currentY -= dY;
            //Updates values and adds a new point to the list
            input.add(startIndex++, new PointF((float) currentX, (float) currentY));
            i--;
        }
        return pointsAdded;
    }

    /**
     * Computes how many points will be needed when adding points in a given distance
     * and given difference between each point without creating a small remainder at
     * the end. If the remainder is greater than the difference between each point over
     * two then return the amount of points which will fit in the distance.
     * Otherwise return the amount which fits the distance minus one.
     *
     * @param D Distance in which to add points
     * @return The amount of points which should be added.
     */
    public int pointsToAdd(Double D)
    {
        int i = 0;
        while (D > DMax) {
            D -= DMax;
            i++;
        }
        if (D >= DMax / 2) { return i; }
        else { return (i - 1); }
    }
}
