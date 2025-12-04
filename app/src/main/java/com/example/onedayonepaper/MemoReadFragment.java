package com.example.onedayonepaper;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.response.BookTotalPageResponse;
import com.example.onedayonepaper.data.dto.response.MemoListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoReadFragment extends Fragment {

    private ReadMemoAdapter adapter;

    private EditText pageSearchEdit;

    private int totalPageCount = 0;
    private int currentPage = 1;

    private int bookId;
    private String bookTitle;
    private String bookAuthor;

    private TextView emptyMemoText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            bookId = getArguments().getInt("bookId");
            bookTitle = getArguments().getString("bookTitle");
            bookAuthor = getArguments().getString("bookAuthor");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memo_read, container, false);

        TextView titleText = view.findViewById(R.id.bookTitle);
        TextView botInfoTitle = view.findViewById(R.id.botInfoTitle);
        TextView botInfoAuthor = view.findViewById(R.id.botInfoAuthor);

        titleText.setText(bookTitle);
        botInfoTitle.setText(bookTitle);
        botInfoAuthor.setText(bookAuthor);

        view.findViewById(R.id.backBtn).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        pageSearchEdit = view.findViewById(R.id.pageSearchEdit);

        emptyMemoText = view.findViewById(R.id.emptyMemoText);

        RecyclerView recyclerView = view.findViewById(R.id.memoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReadMemoAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        Button btnPrev = view.findViewById(R.id.goPre);
        Button btnNext = view.findViewById(R.id.goNext);

        btnPrev.setOnClickListener(v -> {
            movePage(-1);
            pageSearchEdit.setText(String.valueOf(currentPage));
        });

        btnNext.setOnClickListener(v -> {
            movePage(1);
            pageSearchEdit.setText(String.valueOf(currentPage));
        });

        Button memoWriteBtn = view.findViewById(R.id.memo_write);
        memoWriteBtn.setOnClickListener(v -> openMemoWritePage());

        loadTotalPage();

        return view;
    }

    private void loadTotalPage() {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getTotalPage(bookId).enqueue(new Callback<BookTotalPageResponse>() {
            @Override
            public void onResponse(Call<BookTotalPageResponse> call,
                                   Response<BookTotalPageResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    totalPageCount = response.body().getData();

                    pageSearchEdit.setText(String.valueOf(currentPage));

                    setupPageSearch();
                }
            }

            @Override
            public void onFailure(Call<BookTotalPageResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setupPageSearch() {

        pageSearchEdit.setOnEditorActionListener((v, actionId, event) -> {
            String input = v.getText().toString();

            if (input.isEmpty()) {
                Toast.makeText(requireContext(), "페이지 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return true;
            }

            int page = Integer.parseInt(input);

            if (page < 1) {
                Toast.makeText(requireContext(), "최소 페이지는 1입니다.", Toast.LENGTH_SHORT).show();
                page = 1;
            }

            if (page > totalPageCount) {
                Toast.makeText(requireContext(),
                        "최대 페이지는 " + totalPageCount + "p 입니다.",
                        Toast.LENGTH_SHORT).show();
                page = totalPageCount;
            }

            currentPage = page;

            pageSearchEdit.setText(String.valueOf(page));
            pageSearchEdit.setSelection(String.valueOf(page).length());

            loadMemosByPage(page);

            return true;
        });


        pageSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String filtered = s.toString().replaceAll("[^0-9]", "");

                if (!filtered.equals(s.toString())) {
                    pageSearchEdit.setText(filtered);
                    pageSearchEdit.setSelection(filtered.length());
                    return;
                }

                if (filtered.isEmpty()) return;

                int page = Integer.parseInt(filtered);

                if (page < 1) {
                    Toast.makeText(requireContext(), "최소 페이지는 1입니다.", Toast.LENGTH_SHORT).show();
                    pageSearchEdit.setText("1");
                    pageSearchEdit.setSelection(1);
                    currentPage = 1;
                    loadMemosByPage(1);
                    return;
                }

                if (page > totalPageCount) {
                    Toast.makeText(requireContext(),
                            "최대 페이지는 " + totalPageCount + "p 입니다.",
                            Toast.LENGTH_SHORT).show();

                    pageSearchEdit.setText(String.valueOf(totalPageCount));
                    pageSearchEdit.setSelection(String.valueOf(totalPageCount).length());
                    currentPage = totalPageCount;
                    loadMemosByPage(currentPage);
                }
            }
        });
    }

    private void loadMemosByPage(int page) {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getMemoByPage(bookId, page).enqueue(new Callback<MemoListResponse>() {
            @Override
            public void onResponse(Call<MemoListResponse> call,
                                   Response<MemoListResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<ReadMemo> memos = response.body().getData();

                    if (memos == null || memos.isEmpty()) {
                        showNoMemoView();
                    } else {
                        hideNoMemoView();
                        adapter.updateData(memos);
                    }
                }
            }

            @Override
            public void onFailure(Call<MemoListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void movePage(int direction) {
        int newPage = Math.max(1, Math.min(currentPage + direction, totalPageCount));
        currentPage = newPage;
        loadMemosByPage(newPage);
    }

    private void openMemoWritePage() {
        Fragment fragment = new MemoWriteFragment();

        Bundle args = new Bundle();
        args.putInt("bookId", bookId);
        args.putString("bookTitle", bookTitle);
        args.putString("bookAuthor", bookAuthor);
        args.putInt("currentPage", currentPage);

        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showNoMemoView() {
        emptyMemoText.setVisibility(View.VISIBLE);
        adapter.updateData(new ArrayList<>());
    }

    private void hideNoMemoView() {
        emptyMemoText.setVisibility(View.GONE);
    }
}
