package com.org.disk;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by huchen on 16/5/5.
 */
public class DiskView extends RelativeLayout {

    private ImageView diskIv, pointerIv;

    private ObjectAnimator mDiskAnimator;
    private RotateAnimation mPointerAnmator;
    private RotateAnimation mPAnimator;
    private DiskAnimatorUpdateListener mUpdateListener = new DiskAnimatorUpdateListener();

    private boolean isPlay;
    private boolean isPause;
    private boolean isStop;

    private Paint mPaint;
    private Path mPath;

    private float x = 1000;
    private float y = 0;

    private MyThread myThread = new MyThread();


    public DiskView(Context context) {
        super(context);
        init(context);
    }

    public DiskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DiskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.disk_view, this, true);
        diskIv = (ImageView) findViewById(R.id.disk);
        pointerIv = (ImageView) findViewById(R.id.pointer);

        mDiskAnimator = ObjectAnimator.ofFloat(diskIv, "rotation", 0f, 360f);
        mDiskAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mDiskAnimator.setDuration(5000);
        mDiskAnimator.addUpdateListener(mUpdateListener);

        mPointerAnmator = new RotateAnimation(0f, 30f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        mPAnimator = new RotateAnimation(30f, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        mPointerAnmator.setDuration(5000);
        mPointerAnmator.setFillAfter(true);
        mPAnimator.setDuration(5000);
        mPAnimator.setFillAfter(true);
        mPointerAnmator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPaint = new Paint();
        mPath = new Path();

        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        setWillNotDraw(false);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
    }

    private int indexY = 0;
    private int temp = 0;

    @Override
    protected void onDraw(Canvas canvas) {


        temp += 20;

        mPath.moveTo(x, y);
        mPath.quadTo(x, y, x - temp, y + temp);
        canvas.drawPath(mPath, mPaint);

        x -= temp;
        y += temp;
        postInvalidateDelayed(100);

    }

    public void play() {
        isPlay = true;
        animationStatus();
    }

    public void pause() {
        isPause = true;
        animationStatus();
    }

    public void stop() {
        isStop = true;
        animationStatus();
    }

    public void animationStatus() {
        if (isPlay) {
            if (mUpdateListener.isPause) {
                mUpdateListener.play();
            }
            mDiskAnimator.start();

            pointerIv.startAnimation(mPointerAnmator);

            resetStatus();


            return;
        }

        if (isPause) {
            mUpdateListener.pause();

            resetStatus();

            return;
        }

        if (isStop) {
            mDiskAnimator.cancel();

            pointerIv.startAnimation(mPAnimator);

            resetStatus();
        }
    }

    public void resetStatus() {
        isPlay = false;
        isPause = false;
        isStop = false;
    }

    class DiskAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private boolean isPause = false;
        private boolean isPaused = false;
        private float mFraction = 0.0f;
        private long mCurrentPlayTime = 0l;

        public void pause() {
            isPause = true;
        }

        public void play() {
            isPause = false;
            isPaused = false;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isPause) {
                if (!isPaused) {
                    mCurrentPlayTime = animation.getCurrentPlayTime();
                    mFraction = animation.getAnimatedFraction();
                    animation.setInterpolator(new TimeInterpolator() {
                        @Override
                        public float getInterpolation(float input) {
                            return mFraction;
                        }
                    });
                    isPaused = true;
                }
                new CountDownTimer(ValueAnimator.getFrameDelay(), ValueAnimator.getFrameDelay()) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        mDiskAnimator.setCurrentPlayTime(mCurrentPlayTime);
                    }
                }.start();
            } else {
                animation.setInterpolator(null);
            }
        }

    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();

            float index = 0;

            while (x < index) {

                try {
                    Thread.sleep(100);  //慢点更新
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();//View里面的方法, 让重画, 及重新调用onDraw方法. 因为y已经更新.
            }
        }
    }
}
