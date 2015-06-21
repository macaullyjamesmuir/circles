package perfectpi.circles;

import android.content.Context;
import android.content.res.TypedArray;
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
    private final int   fillColor;
    private final int   strokeColor;


    /**
     * Create a new ImperfectCircleView.
     * @param context Something to pass to super().
     * @param attrs   The style attributes to use.
     */
    public ImperfectCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        paint = new Paint();

        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ImperfectCircleView,
                0,
                0
        );

        try {
            fillColor   = Color.parseColor(styledAttributes.getString(R.styleable.ImperfectCircleView_imperfectCircleFillColor));
            strokeColor = Color.parseColor(styledAttributes.getString(R.styleable.ImperfectCircleView_imperfectCircleStrokeColor));
            paint.setStrokeWidth(styledAttributes.getFloat(R.styleable.ImperfectCircleView_imperfectCircleStrokeWidth, 0));
        } finally {
            styledAttributes.recycle();
        }

    }

    /**
     * Draw the path to the canvas.
     * @param canvas A canvas to draw to.
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(fillColor);
        canvas.drawPath(path, paint);
    }

    /**
     * Set the imperfect circle to draw. The new circle will be
     * drawn immediately.
     * @param imperfectCircle An imperfect circle.
     */
    public void setImperfectCircle(@NonNull ImperfectCircle imperfectCircle)
    {
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
