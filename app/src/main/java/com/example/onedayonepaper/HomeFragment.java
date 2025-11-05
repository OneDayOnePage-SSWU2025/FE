package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager;
    private LinearLayout indicator;

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

        List<Integer> characterList = Arrays.asList(
                R.drawable.monkey_adult,
                R.drawable.monkey_baby,
                R.drawable.monkey_egg
        );
        List<String> clubNames = Arrays.asList("게롱단", "쇼비즈니스맙소사", "착 한 아 이");
        List<String> ranks = Arrays.asList("상위 18%", "상위 23%", "상위 10%");

        HomePagerAdapter adapter = new HomePagerAdapter(characterList, clubNames, ranks);
        viewPager.setAdapter(adapter);

        int childCount = indicator.getChildCount();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < childCount; i++) {
                    View dot = indicator.getChildAt(i);
                    if (i == position) {
                        dot.setBackgroundResource(R.drawable.dot_active);
                    } else {
                        dot.setBackgroundResource(R.drawable.dot_inactive);
                    }
                }
            }
        });
    }
}
