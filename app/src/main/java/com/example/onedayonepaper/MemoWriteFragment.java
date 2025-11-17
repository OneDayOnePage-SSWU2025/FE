package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MemoWriteFragment extends Fragment {
    private Spinner pageSpinner;
    private ImageView profilePreview;
    private int totalPageCount = 140;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memo_write, container, false);

        pageSpinner = view.findViewById(R.id.pageSpinner);
        profilePreview = view.findViewById(R.id.profilePreview);

             String imageUrl = "https://example.com/user/profile.jpg"; // 실제 서버 URL로 변경
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.bg_circle_green) // 로딩 중
                .error(R.drawable.bg_circle_green)       // 실패 시
                .circleCrop()                            // 원형 자르기
                .into(profilePreview);
        List<String> pages = new ArrayList<>();
        for (int i = 1; i <= totalPageCount; i++) {
            pages.add(i + " p");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_spinner_dropdown,
                pages
        );
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        pageSpinner.setAdapter(adapter);

        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPage = pages.get(position);
                Toast.makeText(requireContext(), selectedPage + " 선택됨", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return inflater.inflate(R.layout.fragment_memo_write, container, false);
    }

}