package com.star.draganddraw;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class BoxDrawingView extends View {

    public static final String TAG = "BoxDrawingView";

    public static final String BUNDLE_KEY = "bundleKey";

    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<>();

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF curr = new PointF(event.getX(), event.getY());

        Log.i(TAG, "Received event at x=" + curr.x + ", y=" + curr.y + ":");

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, " ACTION_DOWN");
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, " ACTION_MOVE");
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(curr);
                    if (event.getPointerCount() == 2) {
                        int angle = (int) Math.toDegrees(Math.atan((event.getY(1) - event.getY(0)) /
                                        (event.getX(1) - event.getX(0))));
                        mCurrentBox.setAngle(angle);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, " ACTION_UP");
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, " ACTION_CANCEL");
                mCurrentBox = null;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.save();

            int centerX = (int) (box.getOrigin().x + box.getCurrent().x) / 2;
            int centerY = (int) (box.getOrigin().y + box.getCurrent().y) / 2;
            canvas.rotate(box.getAngle(), centerX, centerY);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);

            canvas.restore();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, super.onSaveInstanceState());
        bundle.putSerializable(BUNDLE_KEY, mBoxes);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mBoxes = (ArrayList<Box>) bundle.getSerializable(BUNDLE_KEY);
            super.onRestoreInstanceState(bundle.getParcelable(BUNDLE_KEY));
//            invalidate();
        }
    }
}
