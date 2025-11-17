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

public class BookRoomFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookroom, container, false);

        Button goMemoButton = view.findViewById(R.id.gomemo);
        goMemoButton.setOnClickListener(v -> {
            Fragment memoFragment = new MemoFragment();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.main_frame, memoFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
