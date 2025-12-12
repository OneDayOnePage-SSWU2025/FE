package com.example.onedayonepaper;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.request.UpdateNicknameRequest;
import com.example.onedayonepaper.data.item.UserInfo;
import com.example.onedayonepaper.data.dto.response.UserInfoResponse;
import com.example.onedayonepaper.data.dto.response.UserUpdateResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInfoFragment extends Fragment {

    ImageButton profileEditBtn;
    ImageView profileImg;
    private Uri selectedProfileUri = null;
    EditText inputNick;
    ApiService apiService;
    String originalNickname = null;


    //갤러리 런처
    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedProfileUri = uri;
                    profileImg.setImageURI(uri);
                }
            });

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_edit_info, container, false);

        view.findViewById(R.id.infoEditBtn).setOnClickListener(v -> {
            //이전 프래그먼트로 돌아가기
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });
        profileEditBtn = view.findViewById(R.id.profileEditBtn);
        profileImg = view.findViewById(R.id.profileImg);
        inputNick = view.findViewById(R.id.inputNick);

        profileEditBtn.setOnClickListener(v -> pickImage.launch("image/*"));

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        View saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> onSaveClicked());

        apiService.getMyInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call,
                                   Response<UserInfoResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    UserInfoResponse body = response.body();

                    if (body.isSuccess()) {

                        UserInfo info = body.getData();

                        String nickname = info.getNickName();
                        String imgUrl = info.getImgUrl();

                        originalNickname = nickname;
                        inputNick.setText(nickname);

                        // 프로필 이미지 표시
                        ImageView profileImg = view.findViewById(R.id.profileImg);
                        Glide.with(requireContext())
                                .load(imgUrl)
                                .into(profileImg);

                    }
                }
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(requireContext(),
                        "서버 연결 실패: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void uploadProfileImage(Uri uri) {
        MultipartBody.Part imagePart = createImagePartFromUri(uri);

        apiService.updateProfileImage(imagePart).enqueue(new Callback<UserUpdateResponse>() {
            @Override
            public void onResponse(Call<UserUpdateResponse> call,
                                   Response<UserUpdateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserUpdateResponse body = response.body();
                    if (body.isSuccess()) {
                        String newUrl = body.getData();

                        Glide.with(requireContext())
                                .load(newUrl)
                                .into(profileImg);
                    } else {
                        Toast.makeText(requireContext(),
                                "이미지 업로드 실패: " + body.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(),
                            "이미지 업로드 실패: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserUpdateResponse> call, Throwable t) {
                Toast.makeText(requireContext(),
                        "서버 연결 실패: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadProfileImageAndNickname(Uri uri, String nickname) {
        MultipartBody.Part imagePart = createImagePartFromUri(uri);

        apiService.updateProfileImage(imagePart).enqueue(new Callback<UserUpdateResponse>() {
            @Override
            public void onResponse(Call<UserUpdateResponse> call,
                                   Response<UserUpdateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserUpdateResponse body = response.body();
                    if (body.isSuccess()) {
                        String newUrl = body.getData();

                        Glide.with(requireContext())
                                .load(newUrl)
                                .into(profileImg);

                        updateNicknameOnly(nickname);

                    } else {
                        Toast.makeText(requireContext(),
                                "이미지 업로드 실패: " + body.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(),
                            "이미지 업로드 실패: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserUpdateResponse> call, Throwable t) {
                Toast.makeText(requireContext(),
                        "이미지 업로드 서버 에러: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private MultipartBody.Part createImagePartFromUri(Uri uri) {
        try {
            String fileName = "profile_" + System.currentTimeMillis() + ".jpg";

            InputStream is = requireContext().getContentResolver().openInputStream(uri);
            byte[] bytes = readAllBytes(is);

            RequestBody requestBody =
                    RequestBody.create(bytes, MediaType.parse("image/*"));

            return MultipartBody.Part.createFormData(
                    "image",
                    fileName,
                    requestBody
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    private void onSaveClicked() {
        String newNick = inputNick.getText().toString().trim();
        boolean imageChanged = (selectedProfileUri != null);
        boolean nickChanged;

        if (originalNickname == null) {
            nickChanged = !newNick.isEmpty();
        } else {
            nickChanged = !newNick.equals(originalNickname);
        }

        // 아무 것도 안 바귐
        if (!imageChanged && !nickChanged) {
            Toast.makeText(requireContext(),
                    "변경된 내용이 없음",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //이미지 닉넴 둘 다 바꿈
        if (imageChanged && nickChanged) {
            uploadProfileImageAndNickname(selectedProfileUri, newNick);
            return;
        }

        // 이미지만 바뀜
        if (imageChanged) {
            uploadProfileImage(selectedProfileUri);
            return;
        }

        //닉네임만 바꿈
        updateNicknameOnly(newNick);
    }

    private void updateNicknameOnly(String nickname) {
        UpdateNicknameRequest req = new UpdateNicknameRequest(nickname);

        apiService.updateNickname(req).enqueue(new Callback<UserUpdateResponse>() {
            @Override
            public void onResponse(Call<UserUpdateResponse> call,
                                   Response<UserUpdateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserUpdateResponse body = response.body();
                    if (body.isSuccess()) {

                        originalNickname = nickname;  // 로컬 값도 업데이트
                        requireActivity()
                                .getSupportFragmentManager()
                                .popBackStack();
                    } else {
                        Toast.makeText(requireContext(),
                                "닉네임 수정 실패: " + body.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(),
                            "닉네임 수정 실패: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserUpdateResponse> call, Throwable t) {
                Toast.makeText(requireContext(),
                        "닉네임 수정 서버 에러: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }




}
