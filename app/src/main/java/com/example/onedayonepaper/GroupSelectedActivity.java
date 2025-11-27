package com.example.onedayonepaper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class GroupSelectedActivity extends AppCompatActivity {

    private List<GroupItem> groupList = new ArrayList<>();

    LinearLayout layoutGroupTabs;
    ImageView ivBookCover;
    TextView tvBookTitle, tvBookAuthor, tvStartDate, tvLatestPage;
    AppCompatButton btnAddGroup;

    int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_selectd);

        selectedIndex = getIntent().getIntExtra("groupIndex", 0);

        layoutGroupTabs = findViewById(R.id.layoutGroupTabs);
        btnAddGroup = findViewById(R.id.btnAddGroup);

        ivBookCover = findViewById(R.id.ivBookCover);
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvBookAuthor = findViewById(R.id.tvBookAuthor);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvLatestPage = findViewById(R.id.tvLatestPage);

        btnAddGroup.setOnClickListener(v -> {
            startActivity(new Intent(this, MainSelectFragment.class));
            finish();
        });

        loadGroupDetail();
    }

    private void loadGroupDetail() {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.getGroupDetail().enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    groupList = response.body().getData();

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

    private void setupGroupButtons() {

        layoutGroupTabs.removeAllViews();

        for (int i = 0; i < groupList.size(); i++) {
            GroupItem item = groupList.get(i);

            AppCompatButton btn = new AppCompatButton(this);
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
