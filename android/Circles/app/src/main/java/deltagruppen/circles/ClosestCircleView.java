package deltagruppen.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * A view containing the "closest" circle to an imperfect
 * circle. Closest is defined as the circle with the same
 * area as the imperfect circle and midpoint in the centroid
 * of the imperfect circle.
 */
public class ClosestCircleView
    extends View
{
    private final Path  path;
    private final Paint paint;

    /**
     * Create a new ClosestCircleView.
     * @param context Something to pass to super().
     * @param attrs   The style attributes to use.
     */
    public ClosestCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
    }

    /**
     * Draw the path to the canvas.
     * @param canvas A canvas to draw to.
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawPath(path, paint);
    }

    /**
     * Set the imperfect circle that the closest circle should be drawn from.
     * The new closest circle will be drawn immediately.
     * @param imperfectCircle An imperfect circle.
     */
    public void setImperfectCircle(@NonNull ImperfectCircle imperfectCircle)
    {
        PointF midPoint = imperfectCircle.getCentroid();

        path.addCircle(
            midPoint.x,
            midPoint.y,
            (float) Math.sqrt(Math.abs(imperfectCircle.getArea()) / Math.PI),
            Path.Direction.CW
        );

        invalidate();
    }

    /**
     * Clear the view.
     */
    public void clear()
    {
        path.reset();
        invalidate();
    }

}
