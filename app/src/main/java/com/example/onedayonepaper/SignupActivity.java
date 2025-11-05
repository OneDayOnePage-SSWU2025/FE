package com.example.onedayonepaper;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import android.view.inputmethod.EditorInfo;

public class SignupActivity extends AppCompatActivity {

    private ViewFlipper flipper;
    private ProgressBar prog;
    private Button checkBtn;

    private EditText etId, etPwd, etNic;
    private ImageButton addProfileBtn, backBtn;
    private ImageView profilePreview;

    private Uri selectedProfileUri = null;

    private final int TOTAL_STEPS = 4; // 0:id,1:pwd,2:nic,3:profile

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

        // 초기 상태
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
                if (isValidId(etId.getText().toString().trim())) goNext();
                else etId.setError("아이디는 5글자 이상");
            } else if (step == 1) {
                if (isValidPwd(etPwd.getText().toString())) goNext();
                else etPwd.setError("비밀번호는 9글자 이상");
            } else if (step == 2) {
                if (isValidNic(etNic.getText().toString().trim())) goNext();
                else etNic.setError("닉네임은 2글자 이상");
            } else if (step == 3) {
                if (isProfileSelected()) {
                    // TODO: 회원가입 완료 처리
                } else {
                    // TODO: 프로필 선택 유도 메시지
                }
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
        // 새 단계 진입 시 버튼 비활성화
        checkBtn.setEnabled(false);
        // 현재 값이 유효하면 활성화
        updateButtonEnabled();
        // 포커스 이동
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
        // 이미지 버튼
    }

    private void updateButtonEnabled() {
        int step = getStep();
        boolean enable;
        if (step == 0) {
            enable = isValidId(etId.getText().toString().trim());
        } else if (step == 1) {
            enable = isValidPwd(etPwd.getText().toString());
        } else if (step == 2) {
            enable = isValidNic(etNic.getText().toString().trim());
        } else {
            enable = isProfileSelected();
        }
        checkBtn.setEnabled(enable);
    }

    private void hideKeyboard(View v) {
        android.view.inputmethod.InputMethodManager imm =
                (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //검증 규칡
    private boolean isValidId(String s) { return s != null && s.length() > 4; }
    private boolean isValidPwd(String s) { return s != null && s.length() > 8; }
    private boolean isValidNic(String s) { return s != null && s.length() > 2; }
    private boolean isProfileSelected() { return selectedProfileUri != null; }
}