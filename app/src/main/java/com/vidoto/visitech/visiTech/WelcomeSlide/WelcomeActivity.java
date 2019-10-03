package com.vidoto.visitech.visiTech.WelcomeSlide;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(WelcomeActivity.this, IntroActivity.class));
        finish();

    }

}
