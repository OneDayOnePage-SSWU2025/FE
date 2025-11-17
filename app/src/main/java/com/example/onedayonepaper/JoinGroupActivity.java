package com.example.onedayonepaper;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class JoinGroupActivity extends AppCompatActivity {

    private EditText etName, etCode;
    private AppCompatButton btnCreate;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_joingroup);

        etName = findViewById(R.id.etName);
        etCode = findViewById(R.id.etCode);
        btnCreate = findViewById(R.id.btnCreate);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());

        btnCreate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String code = etCode.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "교환독서방 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(code)) {
                Toast.makeText(this, "참가 코드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this,
                    "이름: " + name + "\n참가코드: " + code + "\n가입완료",
                    Toast.LENGTH_LONG).show();

            // TODO: 서버 연결하기.......
        });
    }
}