package com.example.onedayonepaper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.item.HomeGroupItem;
import com.example.onedayonepaper.data.dto.response.HomeResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager;
    private LinearLayout indicator;
    private View layoutEmpty;

    private HomePagerAdapter adapter;
    private List<HomeGroupItem> groupList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPagerScene);
        indicator = view.findViewById(R.id.indicator);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);

        adapter = new HomePagerAdapter(groupList);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicatorActive(position);
            }
        });

        fetchHomeGroups();
    }

    private void fetchHomeGroups() {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getHomeGroups().enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<HomeGroupItem> data = response.body().getData();
                    Log.d("API_RESULT", "data = " + new Gson().toJson(data));

                    if (data == null || data.isEmpty()) {
                        showEmptyView();
                    } else {
                        showDataView(data);
                    }

                } else {
                    Log.e("API", "Response error: " + response.code());
                    showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                t.printStackTrace();
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        layoutEmpty.setVisibility(View.VISIBLE);

        viewPager.setVisibility(View.GONE);
        indicator.setVisibility(View.GONE);
    }

    private void showDataView(List<HomeGroupItem> data) {
        layoutEmpty.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        indicator.setVisibility(View.VISIBLE);

        groupList.clear();
        groupList.addAll(data);
        adapter.notifyDataSetChanged();

        updateIndicatorDots();
    }

    private void updateIndicatorDots() {
        indicator.removeAllViews();

        for (int i = 0; i < groupList.size(); i++) {
            View dot = new View(getContext());

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(20, 20);
            params.setMargins(10, 0, 10, 0);

            dot.setLayoutParams(params);
            dot.setBackgroundResource(i == 0 ?
                    R.drawable.dot_active :
                    R.drawable.dot_inactive);

            indicator.addView(dot);
        }
    }
    private void updateIndicatorActive(int position) {
        for (int i = 0; i < indicator.getChildCount(); i++) {
            View dot = indicator.getChildAt(i);
            dot.setBackgroundResource(i == position ?
                    R.drawable.dot_active :
                    R.drawable.dot_inactive);
        }
    }
}
