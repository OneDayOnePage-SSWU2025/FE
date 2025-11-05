package com.example.onedayonepaper;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText inputId, inputPwd;
    Button checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sb = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom);
            return insets;
        });

        inputId = findViewById(R.id.inputId);
        inputPwd = findViewById(R.id.inputPwd);
        checkBtn = findViewById(R.id.checkBtn);

        checkBtn.setEnabled(false);

        TextWatcher watcher = simpleWatcher(this::validateAndToggle);
        inputId.addTextChangedListener(watcher);
        inputPwd.addTextChangedListener(watcher);

        inputId.setOnEditorActionListener((v, actionId, e) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                validateAndToggle();
                return true;
            }
            return false;
        });
        inputPwd.setOnEditorActionListener((v, actionId, e) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                validateAndToggle();
                return true;
            }
            return false;
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 로그인 정보 확인 후 뷰 체인지
            }
        });
    }

    private void validateAndToggle() {
        String id  = inputId.getText().toString().trim();
        String pwd = inputPwd.getText().toString().trim();

        boolean idOk  = id.length() >= 5;
        boolean pwdOk = pwd.length() >= 9;

        inputId.setActivated(idOk);
        inputPwd.setActivated(pwdOk);

        checkBtn.setEnabled(idOk && pwdOk);
    }

    private TextWatcher simpleWatcher(Runnable after) {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { if (after != null) after.run(); }
        };
    }
}
