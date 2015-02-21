package deltagruppen.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View
{
    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i("Touch down", "x: " + event.getX() + ", y: " + event.getY());
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            for (int i = event.getHistorySize() - 1; i >= 0; i--) {
                Log.i("Touch move",
                        "x: " + event.getHistoricalX(i) + ", y: " + event.getHistoricalY(i)
                );
            }
            Log.i("Touch move", "x: " + event.getX() + ", y: " + event.getY());
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i("Touch up", "x: " + event.getX() + ", y: " + event.getY());
            return true;
        }
        return false;
    }
}