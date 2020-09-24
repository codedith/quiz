package com.xcrino.mcqproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcrino.mcqproject.R;

public class TermsAndConditionsActivity extends AppCompatActivity {

    private TextView toolbar_title;
    private ImageView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);

        toolbar_title.setText("Privacy Policy");
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }
}
