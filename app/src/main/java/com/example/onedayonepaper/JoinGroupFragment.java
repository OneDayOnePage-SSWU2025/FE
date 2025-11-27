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

import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        btnCreate.setOnClickListener(v -> joinGroup());

        return view;
    }

    private void joinGroup() {
        String name = etName.getText().toString().trim();
        String codeStr = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(requireContext(),
                    "교환독서방 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(codeStr)) {
            Toast.makeText(requireContext(),
                    "참가 코드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int code;
        try {
            code = Integer.parseInt(codeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(),
                    "참가 코드는 숫자만 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.joinGroup(name, code).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(),
                            "그룹 가입 성공!", Toast.LENGTH_SHORT).show();

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_frame, new BookRoomFragment())
                            .addToBackStack(null)
                            .commit();

                } else {
                    Toast.makeText(requireContext(),
                            "가입 실패… 그룹명 또는 코드가 맞는지 확인해주세요.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(requireContext(),
                        "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
