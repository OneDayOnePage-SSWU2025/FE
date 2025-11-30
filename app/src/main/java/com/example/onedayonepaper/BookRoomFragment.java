package com.example.onedayonepaper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.response.GroupsResponse;
import com.example.onedayonepaper.data.item.GroupItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRoomFragment extends Fragment {

    private List<GroupItem> groupList = new ArrayList<>();

    LinearLayout layoutGroupTabs;
    ImageView ivBookCover;
    TextView tvBookTitle, tvBookAuthor, tvStartDate, tvLatestPage;
    AppCompatButton btnAddGroup, btnGoMemo;

    int selectedIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.group_selectd, container, false);

        layoutGroupTabs = view.findViewById(R.id.layoutGroupTabs);
        btnAddGroup = view.findViewById(R.id.btnAddGroup);
        btnGoMemo = view.findViewById(R.id.gomemo);

        ivBookCover = view.findViewById(R.id.ivBookCover);
        tvBookTitle = view.findViewById(R.id.tvBookTitle);
        tvBookAuthor = view.findViewById(R.id.tvBookAuthor);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvLatestPage = view.findViewById(R.id.tvLatestPage);

        btnAddGroup.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, new MainSelectFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnGoMemo.setOnClickListener(v -> {

            GroupItem item = groupList.get(selectedIndex);

            int bookId = item.getBook().getBookId();
            String bookTitle = item.getBook().getBookTitle();
            String bookAuthor = item.getBook().getAuthor();

            Fragment memoFragment = new MemoFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("bookId", bookId);
            bundle.putString("bookTitle", bookTitle);
            bundle.putString("bookAuthor", bookAuthor);

            memoFragment.setArguments(bundle);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, memoFragment)
                    .addToBackStack(null)
                    .commit();
        });

        loadGroupDetail();

        return view;
    }

    private void loadGroupDetail() {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getGroupDetail().enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    groupList = response.body().getData();

                    if (groupList == null || groupList.isEmpty()) {
                        showEmptyState();
                        return;
                    }
                    setupGroupButtons();
                    displayGroup(selectedIndex);
                    updateButtonUI(selectedIndex);
                }
            }

            @Override
            public void onFailure(Call<GroupsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void showEmptyState() {
        tvBookTitle.setText("참여 중인 독서모임이 없어요");
        tvBookAuthor.setText("");
        tvStartDate.setText("");
        tvLatestPage.setText("");

        ivBookCover.setVisibility(View.INVISIBLE);

        btnGoMemo.setEnabled(false);
        btnGoMemo.setAlpha(0.4f);
        btnGoMemo.setClickable(false);

        layoutGroupTabs.removeAllViews();
    }

    private void setupGroupButtons() {
        layoutGroupTabs.removeAllViews();

        for (int i = 0; i < groupList.size(); i++) {
            GroupItem item = groupList.get(i);

            AppCompatButton btn = new AppCompatButton(requireContext());
            btn.setText(item.getGroupName());
            btn.setTextSize(13);
            btn.setPadding(20, 10, 20, 10);
            btn.setBackgroundResource(R.drawable.tab_unselected_bg);
            btn.setTextColor(Color.GRAY);

            int index = i;

            btn.setOnClickListener(v -> {
                selectedIndex = index;
                displayGroup(index);
                updateButtonUI(index);
            });

            layoutGroupTabs.addView(btn);
        }
    }

    private void displayGroup(int index) {
        GroupItem item = groupList.get(index);

        tvBookTitle.setText(item.getBook().getBookTitle());
        tvBookAuthor.setText(item.getBook().getAuthor());
        tvLatestPage.setText("최근 메모 페이지 " + item.getLatestMemoPage() + "p");
        tvStartDate.setText("독서 시작일 " + item.getBook().getCreatedDate().substring(0, 10));

        Glide.with(this)
                .load(item.getBook().getImgUrl())
                .into(ivBookCover);
    }

    private void updateButtonUI(int selectedIndex) {
        for (int i = 0; i < layoutGroupTabs.getChildCount(); i++) {
            AppCompatButton btn = (AppCompatButton) layoutGroupTabs.getChildAt(i);

            if (i == selectedIndex) {
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundResource(R.drawable.tab_selected_bg);
            } else {
                btn.setTextColor(Color.GRAY);
                btn.setBackgroundResource(R.drawable.tab_unselected_bg);
            }
        }
    }
}
