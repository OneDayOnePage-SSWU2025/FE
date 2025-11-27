package com.example.onedayonepaper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

public class JoinGroupFragment extends Fragment {

    private EditText etName, etCode;
    private AppCompatButton btnCreate;
    private ImageButton backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.group_joingroup, container, false);

        etName = view.findViewById(R.id.etName);
        etCode = view.findViewById(R.id.etCode);
        btnCreate = view.findViewById(R.id.btnCreate);
        backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        btnCreate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String code = etCode.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(),
                        "교환독서방 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(code)) {
                Toast.makeText(requireContext(),
                        "참가 코드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(requireContext(),
                    "이름: " + name + "\n참가코드: " + code + "\n가입완료",
                    Toast.LENGTH_LONG).show();

            // TODO: 서버 연결 필요
        });

        return view;
    }
}
