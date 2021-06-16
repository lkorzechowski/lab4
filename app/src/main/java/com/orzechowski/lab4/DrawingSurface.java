package com.orzechowski.lab4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private final SurfaceHolder mContainer;
    private boolean mThreadOn = false;
    private final Paint mPaint;
    private Bitmap mBitmap;
    private Path mPath;
    private float mCurrentX, mCurrentY, mStartX, mStartY;
    private final List<PreviousPath> previousPaths;

    public DrawingSurface(Context context, AttributeSet attributes){
        super(context, attributes);
        mContainer = getHolder();
        mContainer.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPath = new Path();
        previousPaths = new LinkedList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void run(){
        Canvas canvas;
        while(mThreadOn){
            if(mContainer.getSurface().isValid()) {
                canvas = mContainer.lockCanvas();
                canvas.save();
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(mBitmap, 0, 0, null);
                int currentColor = mPaint.getColor();
                for (PreviousPath p : previousPaths){
                    mPaint.setColor(p.color);
                    canvas.drawCircle(p.startX, p.startY, 10, mPaint);
                    canvas.drawPath(p.path, mPaint);
                    canvas.drawCircle(p.endX, p.endY, 10, mPaint);
                }
                mPaint.setColor(currentColor);
                canvas.drawCircle(mStartX, mStartY, 10, mPaint);
                canvas.drawCircle(mCurrentX, mCurrentY, 10, mPaint);
                canvas.drawPath(mPath, mPaint);
                canvas.restore();
                mContainer.unlockCanvasAndPost(canvas);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        performClick();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                mStartX = mCurrentX;
                mStartY = mCurrentY;
                invalidate();
                renewDrawing();
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.moveTo(event.getX(), event.getY());
                mPath.lineTo(mCurrentX, mCurrentY);
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                previousPaths.add(new PreviousPath(mPath, mPaint.getColor(), mStartX, mStartY, mCurrentX, mCurrentY));
                mThreadOn = false;
                invalidate();
                break;
            default:
        }
        return true;
    }

    public static class PreviousPath{
        Path path;
        int color;
        float startX, startY, endX, endY;

        public PreviousPath(Path pth, int clr, float stX, float stY, float enX, float enY){
            this.path = pth;
            this.color = clr;
            this.startX = stX;
            this.startY = stY;
            this.endX = enX;
            this.endY = enY;
        }
    }

    public boolean performClick(){
        return super.performClick();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Canvas canvas;
        if(mContainer.getSurface().isValid()) {
            canvas = mContainer.lockCanvas();
            canvas.save();
            canvas.drawColor(Color.WHITE);
            canvas.restore();
            mContainer.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        mThreadOn = false;
    }

    public void renewDrawing(){
        mThreadOn = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void setPaintColor(int color){
        mPaint.setColor(color);
    }

    public void deleteDrawing(){
        previousPaths.clear();
        surfaceCreated(mContainer);
    }
}
