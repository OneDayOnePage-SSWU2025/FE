package com.example.onedayonepaper;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

public class EditInfoFragment extends Fragment {

    ImageButton profileEditBtn;
    ImageView profilePreview;
    private Uri selectedProfileUri = null;
    EditText inputNic;

    //갤러리 런처
    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedProfileUri = uri;
                    profilePreview.setImageURI(uri);
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
        profilePreview = view.findViewById(R.id.profilePreview);

        profileEditBtn.setOnClickListener(v -> pickImage.launch("image/*"));

        return view;
    }
}
