package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MemoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        Button btnWrite = view.findViewById(R.id.memo_write);
        Button btnRead = view.findViewById(R.id.memo_read);
        Button btnMy = view.findViewById(R.id.memo_my);


        btnWrite.setOnClickListener(v -> {
            Fragment memoWriteFragment = new MemoWriteFragment();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.main_frame, memoWriteFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        btnRead.setOnClickListener(v -> {
            Fragment memoReadFragment = new MemoReadFragment();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.main_frame, memoReadFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // 내 메모 화면으로 이동
        btnMy.setOnClickListener(v -> {
            Fragment memoMyFragment = new MemoMyFragment();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.main_frame, memoMyFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
