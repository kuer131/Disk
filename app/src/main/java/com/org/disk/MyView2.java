package com.org.disk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huchen on 2016/5/8 0008.
 */
public class MyView2 extends View {

    Bitmap src_bitmap;
    int x;
    int rate = 5;

    public MyView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        src_bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.video_music_indicator);
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x123)
                    invalidate();
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (x > src_bitmap.getWidth() - 2*rate)
                    x = 0;
                x += rate;
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 100);

    }

    @Override
    public void onDraw(Canvas canvas) {

        Bitmap tmp_bitmap = Bitmap.createBitmap(src_bitmap, 0, 0, x,
                src_bitmap.getHeight());
        canvas.drawBitmap(tmp_bitmap, 0, 0, null);
        tmp_bitmap.recycle();
    }
}