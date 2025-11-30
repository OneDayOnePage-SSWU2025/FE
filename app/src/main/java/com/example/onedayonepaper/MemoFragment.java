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
        Bundle args = getArguments();
        // 메모 작성하기 페이지로 이동
        btnWrite.setOnClickListener(v -> {

            Fragment memoWriteFragment = new MemoWriteFragment();
            memoWriteFragment.setArguments(args);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, memoWriteFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // 메모 모아보기 화면으로 이동
        btnRead.setOnClickListener(v -> {

            Fragment memoReadFragment = new MemoReadFragment();
            memoReadFragment.setArguments(args);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, memoReadFragment)
                    .addToBackStack(null)
                    .commit();
        });

        //내 메모 모아보기  화면으로 이동
        btnMy.setOnClickListener(v -> {

            Fragment memoMyFragment = new MemoMyFragment();
            memoMyFragment.setArguments(args);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, memoMyFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
