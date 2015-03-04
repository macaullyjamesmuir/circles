package deltagruppen.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * A view of an imperfect circle.
 */
public class ImperfectCircleView
    extends View
{
    private final Path  path;
    private final Paint paint;

    /**
     * Create a new ImperfectCircleView.
     * @param context Something to pass to super().
     * @param attrs   The style attributes to use.
     */
    public ImperfectCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        paint = new Paint();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
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
     * Set the imperfect circle to draw. The new circle will be
     * drawn immediately.
     * @param imperfectCircle An imperfect circle.
     */
    public void setImperfectCircle(@NonNull ImperfectCircle imperfectCircle)
    {
        invalidate();

        List<PointF> points = imperfectCircle.getPoints();

        path.moveTo(points.get(0).x, points.get(0).y);
        for (PointF point : imperfectCircle.getPoints()) {
            path.lineTo(point.x, point.y);
        }

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
