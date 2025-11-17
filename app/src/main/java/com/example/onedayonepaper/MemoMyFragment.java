
package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemoMyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memo_my, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.memoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<MyMemo> memoList = new ArrayList<>();
        memoList.add(new MyMemo("https://example.com/profile1.png", "빵 맛있겠다. 왜냐하면 맛있어보이기 때문이다.", "130p"));
        memoList.add(new MyMemo("https://example.com/profile2.png", "빵 맛있겠다. 왜냐하면 맛있어보이기 때문이다.", "120p"));

        MyMemoAdapter adapter = new MyMemoAdapter(memoList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
