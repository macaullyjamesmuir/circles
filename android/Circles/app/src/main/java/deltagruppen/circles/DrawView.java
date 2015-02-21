package deltagruppen.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;

public class DrawView extends View
{
    private final LinkedList<PointF> points;
    private final Paint              paint;

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        points = new LinkedList<>();
        paint  = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (points.size() == 0) return;

        Iterator<PointF> iterator = points.iterator();
        PointF point = iterator.next();

        while (iterator.hasNext()) {
            PointF next = iterator.next();
            canvas.drawLine(point.x, point.y, next.x, next.y, paint);
            point = next;
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            points.clear();
            points.add(new PointF(event.getX(), event.getY()));
            invalidate();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            for (int i = event.getHistorySize() - 1; i >= 0; i--) {
                points.add(new PointF(event.getHistoricalX(i), event.getHistoricalY(i)));
            }
            points.add(new PointF(event.getX(), event.getY()));
            invalidate();
            return true;
        }
        return false;
    }
}