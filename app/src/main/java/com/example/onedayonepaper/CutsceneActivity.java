package com.example.onedayonepaper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class CutsceneActivity extends AppCompatActivity {

    private ViewFlipper storyFlipper;
    private static final int TOTAL_SCENES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutscene);

        storyFlipper = findViewById(R.id.storyFlipper);

        storyFlipper.setInAnimation(this, android.R.anim.fade_in);
        storyFlipper.setOutAnimation(this, android.R.anim.fade_out);

        storyFlipper.setOnClickListener(v -> handleSceneTransition());
    }

    private void handleSceneTransition() {
        int currentScene = storyFlipper.getDisplayedChild();

        if (currentScene < TOTAL_SCENES - 1) {
            storyFlipper.showNext();
        } else {
            navigateToMain();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(CutsceneActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}