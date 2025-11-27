package com.example.onedayonepaper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.response.GroupsResponse;
import com.example.onedayonepaper.data.item.GroupItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainSelectFragment extends Fragment {

    private AppCompatButton btnAddGroup, btnMakeGroup, btnJoinGroup;
    private LinearLayout layoutGroupTabsMain;

    private List<GroupItem> groupList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.group_main_select, container, false);

        btnAddGroup = view.findViewById(R.id.btnAddGroup);
        btnMakeGroup = view.findViewById(R.id.btnMakeGroup);
        btnJoinGroup = view.findViewById(R.id.btnJoinGroup);
        layoutGroupTabsMain = view.findViewById(R.id.layoutGroupTabsMain);

        btnAddGroup.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, new MakeGroupFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnMakeGroup.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, new MakeGroupFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnJoinGroup.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, new JoinGroupFragment())
                    .addToBackStack(null)
                    .commit();
        });

        loadGroupList();

        return view;
    }

    private void loadGroupList() {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getGroupDetail().enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    groupList = response.body().getData();
                    setupGroupTabsMain();
                }
            }

            @Override
            public void onFailure(Call<GroupsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setupGroupTabsMain() {

        layoutGroupTabsMain.removeAllViews();

        if (groupList == null || groupList.isEmpty()) return;

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
                BookRoomFragment fragment = new BookRoomFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("groupIndex", index);
                fragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            });

            layoutGroupTabsMain.addView(btn);
        }
    }
}
