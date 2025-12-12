package com.example.onedayonepaper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.response.SignUpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SignupActivity extends AppCompatActivity {

    private ViewFlipper flipper;
    private ProgressBar prog;
    private Button checkBtn;

    private EditText etId, etPwd, etNic;
    private ImageButton addProfileBtn, backBtn;
    private ImageView profilePreview;

    private Uri selectedProfileUri = null;

    private final int TOTAL_STEPS = 4; // 0:id,1:pwd,2:nic,3:profile

    private ApiService authApi;

    // 갤러리 런처
    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedProfileUri = uri;
                    profilePreview.setImageURI(uri);
                    profilePreview.setVisibility(View.VISIBLE);
                    addProfileBtn.setVisibility(View.GONE);
                    updateButtonEnabled();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        flipper = findViewById(R.id.viewFlipper);
        prog = findViewById(R.id.progBar);
        checkBtn = findViewById(R.id.checkBtn);

        etId = findViewById(R.id.inputId);
        etPwd = findViewById(R.id.inputPwd);
        etNic = findViewById(R.id.inputNic);
        addProfileBtn = findViewById(R.id.addProfileBtn);
        profilePreview = findViewById(R.id.profilePreview);
        backBtn = findViewById(R.id.backBtn);

        flipper.setInAnimation(this, android.R.anim.slide_in_left);
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);

        //초기 상태
        setStep(0);

        etId.addTextChangedListener(simpleWatcher(this::updateButtonEnabled));
        etPwd.addTextChangedListener(simpleWatcher(this::updateButtonEnabled));
        etNic.addTextChangedListener(simpleWatcher(this::updateButtonEnabled));

        etId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v);
                v.clearFocus();
                boolean ok = isValidId(etId.getText().toString().trim());
                checkBtn.setEnabled(ok);
                etId.setActivated(ok);
                return true;
            }
            return false;
        });

        etPwd.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v);
                v.clearFocus();
                boolean ok = isValidPwd(etPwd.getText().toString());
                checkBtn.setEnabled(ok);
                etPwd.setActivated(ok);
                return true;
            }
            return false;
        });

        etNic.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v);
                v.clearFocus();
                boolean ok = isValidNic(etNic.getText().toString().trim());
                checkBtn.setEnabled(ok);
                etNic.setActivated(ok);
                return true;
            }
            return false;
        });

        // 프로필 선택
        addProfileBtn.setOnClickListener(v -> pickImage.launch("image/*"));

        // 확인 버튼
        checkBtn.setOnClickListener(v -> {
            int step = getStep();
            if (step == 0) {
                if (isValidId(etId.getText().toString().trim())) {
                    goNext();
                } else {
                    etId.setError("아이디는 3글자 이상");
                }
            } else if (step == 1) {
                if (isValidPwd(etPwd.getText().toString())) {
                    goNext();
                } else {
                    etPwd.setError("비밀번호는 3글자 이상");
                }
            } else if (step == 2) {
                if (isValidNic(etNic.getText().toString().trim())) {
                    goNext();
                } else {
                    etNic.setError("닉네임은 2글자 이상");
                }
            } else if (step == 3) {
                if (!isProfileSelected()) {
                    Toast.makeText(getApplicationContext(), "프로필 사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                authApi = ApiClient.getClient(this).create(ApiService.class);

                String id = etId.getText().toString().trim();
                String pwd = etPwd.getText().toString();
                String nick = etNic.getText().toString().trim();

                RequestBody idBody = RequestBody.create(id, MediaType.parse("text/plain"));
                RequestBody pwdBody = RequestBody.create(pwd, MediaType.parse("text/plain"));
                RequestBody nickBody = RequestBody.create(nick, MediaType.parse("text/plain"));

                MultipartBody.Part imgPart = createImagePartFromUri(selectedProfileUri);
                if (imgPart == null) {
                    return;
                }

                Call<SignUpResponse> call = authApi.signup(idBody, pwdBody, nickBody, imgPart);
                call.enqueue(new retrofit2.Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(Call<SignUpResponse> call,
                                           retrofit2.Response<SignUpResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            SignUpResponse body = response.body();
                            if (body.isSuccess()) {
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "회원가입 실패: " + body.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this,
                                    "회원가입 실패(http): " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                            String errorBody = response.errorBody() != null
                                    ? response.errorBody().toString()
                                    : "no error body";

                            Log.e("SIGNUP", "error = " + errorBody);
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpResponse> call, Throwable t) {
                        Toast.makeText(SignupActivity.this,
                                "네트워크 오류: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        backBtn.setOnClickListener(view -> {
            if (flipper.getDisplayedChild() > 0) { // 첫 화면이 아닐 때만 뒤로
                flipper.showPrevious();
                updateProgress(flipper.getDisplayedChild());
            } else {
                finish();
            }
        });
    }

    private TextWatcher simpleWatcher(Runnable after) {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { if (after != null) after.run(); }
        };
    }

    private int getStep() {
        return flipper.getDisplayedChild();
    }

    private void setStep(int step) {
        flipper.setDisplayedChild(step);
        updateProgress(step);
        checkBtn.setEnabled(false);
        updateButtonEnabled();
        requestFocusForStep(step);
    }

    private void goNext() {
        int next = Math.min(getStep() + 1, TOTAL_STEPS - 1);
        setStep(next);
    }

    private void updateProgress(int step) {
        int percent = (int) Math.round(((step + 1) / (double) TOTAL_STEPS) * 100.0);
        prog.setProgress(percent);
    }

    private void requestFocusForStep(int step) {
        if (step == 0) etId.requestFocus();
        else if (step == 1) etPwd.requestFocus();
        else if (step == 2) etNic.requestFocus();
    }

    private void updateButtonEnabled() {
        int step = getStep();

        boolean idOk  = isValidId(etId.getText().toString().trim());
        boolean pwdOk = isValidPwd(etPwd.getText().toString());
        boolean nicOk = isValidNic(etNic.getText().toString().trim());
        boolean profileOk = isProfileSelected();

        if (step == 0) {
            etId.setActivated(idOk);
        } else if (step == 1) {
            etPwd.setActivated(pwdOk);
        } else if (step == 2) {
            etNic.setActivated(nicOk);
        }

        boolean enable;
        if (step == 0) {
            enable = idOk;
        } else if (step == 1) {
            enable = pwdOk;
        } else if (step == 2) {
            enable = nicOk;
        } else {
            enable = profileOk;
        }

        checkBtn.setEnabled(enable);
    }


    private void hideKeyboard(View v) {
        android.view.inputmethod.InputMethodManager imm =
                (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    // 검증 규칙
    private boolean isValidId(String s) { return s != null && s.length() >= 3; }
    private boolean isValidPwd(String s) { return s != null && s.length() >= 3; }
    private boolean isValidNic(String s) { return s != null && s.length() >= 2; }
    private boolean isProfileSelected() { return selectedProfileUri != null; }

    private MultipartBody.Part createImagePartFromUri(Uri uri) {
        if (uri == null) return null;

        try (InputStream is = getContentResolver().openInputStream(uri);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            byte[] bytes = baos.toByteArray();
            RequestBody requestBody =
                    RequestBody.create(bytes, MediaType.parse("image/*"));

            String fileName = "profile_" + System.currentTimeMillis() + ".jpg";
            return MultipartBody.Part.createFormData(
                    "img",
                    fileName,
                    requestBody
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
