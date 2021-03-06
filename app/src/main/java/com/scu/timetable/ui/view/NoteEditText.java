package com.scu.timetable.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Z-P-J
 */
public class NoteEditText extends AppCompatEditText {

    private Paint paint = new Paint();

    private PathEffect effect = new DashPathEffect(new float[]{10, 10, 5, 5}, 5);

    public NoteEditText(Context context) {
        super(context);
        initPaint();
    }

    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public NoteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.LTGRAY);
        paint.setPathEffect(effect);
        int left = getLeft();
        int right = getRight();
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int height = getHeight();
        int lineHeight = getLineHeight();
        int spacingHeight = (int)getLineSpacingExtra();
        int count = (height - paddingTop - paddingBottom) / lineHeight;
        for (int i = 0; i < count; i++) {
            int baseline = lineHeight * (i + 1) + paddingTop - spacingHeight / 2;
            canvas.drawLine(left + paddingLeft, baseline, right - paddingRight, baseline, paint);
        }
        super.onDraw(canvas);
    }
}
