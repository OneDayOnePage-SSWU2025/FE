package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class EditInfoFragment extends Fragment {
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

        return view;
    }
}
