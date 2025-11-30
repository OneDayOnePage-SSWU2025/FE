package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.response.MyMemoResponse;
import com.example.onedayonepaper.data.item.MemoItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoMyFragment extends Fragment {

    private int bookId;
    private String bookTitle;

    private RecyclerView recyclerView;
    private MyMemoAdapter adapter;
    private TextView emptyMemoText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            bookId = getArguments().getInt("bookId");
            bookTitle = getArguments().getString("bookTitle");
        } else {
            Toast.makeText(requireContext(), "북정보 불러와지지 않음", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memo_my, container, false);

        TextView titleText = view.findViewById(R.id.bookTitle);
        titleText.setText(bookTitle);

        Button backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        recyclerView = view.findViewById(R.id.memoRecyclerView);
        emptyMemoText = view.findViewById(R.id.emptyText);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new MyMemoAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        adapter.setOnMemoClickListener(memo -> openDetailPage(memo));
        loadMyMemo();

        return view;
    }

    private void loadMyMemo() {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getMyMemo(bookId).enqueue(new Callback<MyMemoResponse>() {
            @Override
            public void onResponse(Call<MyMemoResponse> call, Response<MyMemoResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<MemoItem> memoList = response.body().getData();

                    if (memoList == null || memoList.isEmpty()) {
                        emptyMemoText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyMemoText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        adapter.updateData(memoList);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyMemoResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "내 메모 불러오기 실패", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void openDetailPage(MemoItem memo) {
        MemoMyDetailFragment fragment = new MemoMyDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString("profileUrl", memo.getImgUrl());
        bundle.putString("memo", memo.getMemo());
        bundle.putInt("page", memo.getPage());
        bundle.putInt("memoId", memo.getMemoId());
        bundle.putString("bookTitle", bookTitle);
        fragment.setArguments(bundle);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}
