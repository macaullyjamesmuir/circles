package deltagruppen.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.LinkedList;

public class DrawView extends RelativeLayout
{
    private final LinkedList<PointF>  points;
    private Path                      path;
    private final Paint               paint;

    private ImperfectCircleView imperfectCircleView;

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        points = new LinkedList<>();
        paint  = new Paint();
        path   = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onFinishInflate()
    {
        imperfectCircleView = (ImperfectCircleView)findViewById(R.id.imperfectCircleView);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            invalidate();

            points.clear();
            imperfectCircleView.clear();
            path.reset();
            points.add(new PointF(event.getX(), event.getY()));
            path.moveTo(event.getX(), event.getY());

            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                points.add(new PointF(event.getHistoricalX(i), event.getHistoricalY(i)));
                path.lineTo(event.getHistoricalX(i), event.getHistoricalY(i));
            }
            points.add(new PointF(event.getX(), event.getY()));
            path.lineTo(event.getX(), event.getY());
            invalidate();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            try {
                imperfectCircleView.setImperfectCircle(new ImperfectCircle(points));
            }
            catch (IllegalArgumentException e) {
                Log.i("DrawView", "Not a circle :(");
            }
        }
        return false;
    }
}