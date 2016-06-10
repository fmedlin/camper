package org.fmedlin.camper_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fmedlin.camper.sampler.R;

import java.util.Locale;

public class NextActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_RANK = "extra_rank";
    public static final String EXTRA_SERIAL = "extra_serial";

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_next);

        text = (TextView) findViewById(R.id.text);
        text. setText(String.format(Locale.getDefault(),
                "Hello, %s %s - %d",
                intent.getStringExtra(EXTRA_RANK),
                intent.getStringExtra(EXTRA_NAME),
                intent.getLongExtra(EXTRA_SERIAL, 0)));
    }
}
