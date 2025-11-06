package com.example.onedayonepaper;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;


public class OnboardingActivity extends AppCompatActivity {
    private AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ImageView iv = findViewById(R.id.ivHaru);

        anim = (AnimationDrawable) iv.getDrawable();
        iv.post(() -> {
            anim.start();

            int total = 0;
            for (int i = 0; i < anim.getNumberOfFrames(); i++) {
                total += anim.getDuration(i);
            }

            new Handler().postDelayed(() -> {
                startActivity(new android.content.Intent(this, MainActivity.class));
                finish();
            }, total + 300);
        });
    }
}


