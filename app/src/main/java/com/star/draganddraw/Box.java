package com.star.draganddraw;


import android.graphics.PointF;

public class Box {

    private PointF mOrigin;
    private PointF mCurrent;

    private int mAngle;

    public Box(PointF origin) {
        mOrigin = mCurrent = origin;
        mAngle = 0;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public int getAngle() {
        return mAngle;
    }

    public void setAngle(int angle) {
        mAngle = angle;
    }
}
