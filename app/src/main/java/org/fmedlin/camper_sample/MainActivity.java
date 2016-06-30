package org.fmedlin.camper_sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fmedlin.camper.sampler.R;

import org.fmedlin.camper.IntentBuilder;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    Button btnImplicit;
    Button btnChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.start);
        btnImplicit = (Button) findViewById(R.id.implicit);
        btnChooser = (Button) findViewById(R.id.chooser);

        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentBuilder.start(NextActivity.class)
                        .from(MainActivity.this)
                        .extra(NextActivity.EXTRA_NAME, "Chuckles")
                        .extra(NextActivity.EXTRA_RANK, "Mr.")
                        .extra(NextActivity.EXTRA_SERIAL, 466453L)
                        .execute();
            }
        });

        btnImplicit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = IntentBuilder.with(Intent.ACTION_VIEW)
                        .data(Uri.parse("http://google.com"))
                        .build();
                startActivity(intent);
            }
        });

        btnChooser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentBuilder.with(Intent.ACTION_SEND)
                        .extra(Intent.EXTRA_TEXT, "Falling leaves of sycamore")
                        .type("text/plain")
                        .chooser("Pick one:")
                        .execute(MainActivity.this, getPackageManager());
            }
        });
    }
}
