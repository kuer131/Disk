package com.org.disk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv1, tv2;
    DiskView disk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv1.setTextScaleX(2);

        disk = (DiskView) findViewById(R.id.dv);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        disk.play();

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disk.stop();
            }
        });
    }

}
