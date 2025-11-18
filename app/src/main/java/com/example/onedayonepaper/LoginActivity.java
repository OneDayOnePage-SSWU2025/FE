package com.example.onedayonepaper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.LoginRequest;
import com.example.onedayonepaper.data.dto.LoginResponse;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    EditText inputId, inputPwd;
    Button checkBtn;
    ApiService authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        authApi = ApiClient.getClient(this).create(ApiService.class);

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
                String id  = inputId.getText().toString().trim();
                String pwd = inputPwd.getText().toString().trim();

                LoginRequest req = new LoginRequest(id, pwd);

                Call<LoginResponse> call = authApi.login(req);
                call.enqueue(new retrofit2.Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call,
                                           retrofit2.Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse body = response.body();
                            if (body.isSuccess()) {
                                String token = body.getData().toString();   // jwt 토큰

                                //SharedPreferences 저장
                                getSharedPreferences("auth", MODE_PRIVATE)
                                        .edit()
                                        .putString("jwt", token)
                                        .apply();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "아이디 또는 비밀번호가 틀렸습니다",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "로그인 실패: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // 네트워크 에러
                        Toast.makeText(LoginActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void validateAndToggle() {
        String id  = inputId.getText().toString().trim();
        String pwd = inputPwd.getText().toString().trim();

        boolean idOk  = id.length() >= 3;
        boolean pwdOk = pwd.length() >= 3;

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
