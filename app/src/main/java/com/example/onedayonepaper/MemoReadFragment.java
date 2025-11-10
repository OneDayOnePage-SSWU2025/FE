package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemoReadFragment extends Fragment {

    private ReadMemoAdapter adapter;
    private Spinner pageSpinner;

    private List<ReadMemo> allMemos; // 전체 더미 데이터 저장용

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memo_read, container, false);

        pageSpinner = view.findViewById(R.id.pageSpinner);
        RecyclerView recyclerView = view.findViewById(R.id.memoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ReadMemoAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        allMemos = new ArrayList<>();
        allMemos.add(new ReadMemo("https://example.com/profile1.png", "수진", "이 부분 너무 인상 깊어요.", 10));
        allMemos.add(new ReadMemo("https://example.com/profile2.png", "유우시", "여기서 주제 의식이 확 드러나지요.", 10));
        allMemos.add(new ReadMemo("https://example.com/profile3.png", "민수", "감정선이 되게 진정성 있게 느껴졌어요.", 20));
        allMemos.add(new ReadMemo("https://example.com/profile4.png", "혜린", "비유 표현이 너무 예쁘네요.", 30));
        allMemos.add(new ReadMemo("https://example.com/profile5.png", "정빈", "스토리 전개가 매끄럽지 않은 듯.", 20));
        allMemos.add(new ReadMemo("https://example.com/profile6.png", "영호", "결말이 여운을 남겨요.", 30));


        List<String> pages = new ArrayList<>();
        pages.add("전체 보기");
        pages.add("10");
        pages.add("20");
        pages.add("30");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                pages
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(spinnerAdapter);


        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = pages.get(position);
                if (selected.equals("전체 보기")) {
                    adapter.updateData(allMemos);
                } else {
                    int page = Integer.parseInt(selected);
                    List<ReadMemo> filtered = new ArrayList<>();
                    for (ReadMemo m : allMemos) {
                        if (m.getPage() == page) filtered.add(m);
                    }
                    adapter.updateData(filtered);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        adapter.updateData(allMemos);


        //아래 프로그레스바
        SeekBar pageSeekBar = view.findViewById(R.id.pageSeekBar);
        int totalPages = 140;
        pageSeekBar.setMax(totalPages);

        int currentPage = 1;
        pageSeekBar.setProgress(currentPage);

        pageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {

                    int selectedPage = Math.max(progress, 1);


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



        return view;
    }
}
