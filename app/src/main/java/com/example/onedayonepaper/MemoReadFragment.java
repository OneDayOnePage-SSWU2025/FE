
package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import com.example.onedayonepaper.data.item.MemoItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoReadFragment extends Fragment {

    private ReadMemoAdapter adapter;
    private Spinner pageSpinner;
    private SeekBar pageSeekBar;

    private int totalPageCount = 0;
    private int currentPage = 1;

    private List<ReadMemo> allMemos = new ArrayList<>();

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
        } else {
            Toast.makeText(requireContext(), "북정보 불러와지지 않음", Toast.LENGTH_SHORT).show();
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

        Button backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        pageSpinner = view.findViewById(R.id.pageSpinner);
        pageSeekBar = view.findViewById(R.id.pageSeekBar);

        Button btnPrev = view.findViewById(R.id.goPre);
        Button btnNext = view.findViewById(R.id.goNext);
        emptyMemoText = view.findViewById(R.id.emptyMemoText);


        RecyclerView recyclerView = view.findViewById(R.id.memoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ReadMemoAdapter(requireContext(), new ArrayList<>());

        recyclerView.setAdapter(adapter);


        loadTotalPage();

        btnPrev.setOnClickListener(v -> movePage(-1));
        btnNext.setOnClickListener(v -> movePage(1));

        return view;
    }


    private void loadTotalPage() {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getTotalPage(bookId).enqueue(new Callback<BookTotalPageResponse>() {
            @Override
            public void onResponse(Call<BookTotalPageResponse> call, Response<BookTotalPageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    totalPageCount = response.body().getData();

                    setupSpinner(totalPageCount);
                    setupSeekBar(totalPageCount);

                    adapter.updateData(allMemos);
                }
            }

            @Override
            public void onFailure(Call<BookTotalPageResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "페이지 수 불러오기 실패", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void rotateArrow(boolean isOpen) {
        View selected = pageSpinner.getSelectedView();
        if (selected == null) return;

        ImageView arrow = selected.findViewById(R.id.ivArrow);
        if (arrow == null) return;

        float degree = isOpen ? 180f : 0f;

        arrow.animate().rotation(degree).setDuration(200).start();
    }

    private void setupSpinner(int totalPageCount) {

        List<String> pages = new ArrayList<>();
        for (int i = 1; i <= totalPageCount; i++) {
            pages.add(i + " p");
        }

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(requireContext(), pages);
        pageSpinner.setAdapter(spinnerAdapter);

        pageSpinner.post(() -> pageSpinner.setDropDownVerticalOffset(pageSpinner.getHeight()));

        pageSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) rotateArrow(true);
            return false;
        });

        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rotateArrow(false);

                currentPage = position + 1;

                pageSeekBar.setProgress(currentPage);

                loadMemosByPage(currentPage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private void loadMemosByPage(int page) {

        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getMemoByPage(bookId, page).enqueue(new Callback<MemoListResponse>() {
            @Override
            public void onResponse(Call<MemoListResponse> call, Response<MemoListResponse> response) {
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
    private void showNoMemoView() {
        emptyMemoText.setVisibility(View.VISIBLE);
        adapter.updateData(new ArrayList<>());
    }

    private void hideNoMemoView() {
        emptyMemoText.setVisibility(View.GONE);
    }

    private void setupSeekBar(int totalPageCount) {
        pageSeekBar.setMax(totalPageCount);

        pageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;

                int selectedPage = Math.max(progress, 1);

                currentPage = selectedPage;

                pageSpinner.setSelection(selectedPage - 1);

                loadMemosByPage(selectedPage);
            }


            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    private void movePage(int direction) {

        int newPage = currentPage + direction;

        if (newPage < 1) newPage = 1;
        if (newPage > totalPageCount) newPage = totalPageCount;

        currentPage = newPage;

        pageSeekBar.setProgress(newPage);
        pageSpinner.setSelection(newPage - 1);

        loadMemosByPage(newPage);
    }



    private void filterMemosByPage(int page) {
        loadMemosByPage(page);
    }



}
