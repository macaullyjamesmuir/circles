package perfectpi.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

public class DrawView extends RelativeLayout
{
    private final LinkedList<PointF>  points;
    private Path                      path;
    private static int                StrokeWidth = 10; // R.integer.stroke_width;
    private final Paint               paint;

    private ImperfectCircleView imperfectCircleView;
    private ClosestCircleView   closestCircleView;
    private LinearLayout        piCalculationPopup;
    private TextView            piMessagePopup;

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        points = new LinkedList<>();
        paint  = new Paint();
        path   = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(StrokeWidth);
        paint.setColor(getResources().getColor(R.color.circle));
    }

    @Override
    public void onFinishInflate()
    {
        imperfectCircleView = (ImperfectCircleView) findViewById(R.id.imperfectCircleView);
        closestCircleView   = (ClosestCircleView)   findViewById(R.id.closestCircleView);
        piCalculationPopup  = (LinearLayout)        findViewById(R.id.piCalculationPopup);
        piMessagePopup      = (TextView)            findViewById(R.id.piMessagePopup);
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
            closestCircleView.clear();
            piCalculationPopup.setVisibility(View.INVISIBLE);
            piMessagePopup.setVisibility(View.INVISIBLE);
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
                ImperfectCircle imperfectCircle = new ImperfectCircle(points);
                imperfectCircleView.setImperfectCircle(imperfectCircle);
                TextView approximationTextView = (TextView) piCalculationPopup.findViewById(R.id.pi_approximation_field);

                double l = imperfectCircle.getPerimeterLength();
                double a = Math.abs(imperfectCircle.getArea());
                double pi = l*l / (4*a);
                String s = String.valueOf(pi);

                // Insert appropriate separator for number depending on language.
                char c = getResources().getString(R.string.number_separation).charAt(0);
                s = s.replace('.', c);

                // Use at most 10 decimals
                s = s.substring(0, Math.min(s.length(), 10));

                approximationTextView.setText(s);

                piCalculationPopup.setVisibility(View.VISIBLE);
                closestCircleView.setImperfectCircle(imperfectCircle);
            }
            catch (IllegalArgumentException e) {
            }
        }
        if (!piCalculationPopup.isShown()) {
            piMessagePopup.setVisibility(View.VISIBLE);
        }
        return false;
    }
}