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

import com.example.onedayonepaper.data.ApiClient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {
    EditText inputId, inputPwd;
    Button checkBtn;
    AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        authApi = ApiClient.getClient(this).create(AuthApi.class);

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

                Call<ApiResponse> call = authApi.login(req);
                call.enqueue(new retrofit2.Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call,
                                           retrofit2.Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse body = response.body();
                            if (body.isSuccess()) {
                                String token = body.getData();   // jwt 토큰

                                //SharedPreferences 저장
                                getSharedPreferences("auth", MODE_PRIVATE)
                                        .edit()
                                        .putString("jwt", token)
                                        .apply();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                // todo: 로그인 실패 (비밀번호 틀림 등)
                            }
                        } else {
                            // todo: 서버 에러, 응답 실패
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
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

    //백엔드 연결
    public static class LoginRequest {
        String id;
        String password;

        public LoginRequest(String id, String password) {
            this.id = id;
            this.password = password;
        }
    }

    public static class ApiResponse {
        boolean success;
        String message;
        String data; // 토큰 저장

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getData() { return data; }
    }

    public interface AuthApi {
        @POST("/login")
        Call<ApiResponse> login(@Body LoginRequest request);
    }
}
