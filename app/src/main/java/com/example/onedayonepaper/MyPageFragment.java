package com.example.onedayonepaper;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.ReportItem;
import com.example.onedayonepaper.data.dto.ReportResponse;

import java.util.Locale;

import retrofit2.Call;

public class MyPageFragment extends Fragment {

    ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mypage, container, false);

        TextView bookCountTxt = v.findViewById(R.id.bookCountTxt);
        TextView memoCountTxt = v.findViewById(R.id.memoCountTxt);

        apiService = ApiClient.getClient(requireContext()).create(ApiService.class);

        apiService.getReport().enqueue(new retrofit2.Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call,
                                   retrofit2.Response<ReportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReportResponse body = response.body();

                    if (body.isSuccess() && body.getData() != null) {
                        ReportItem item = body.getData();

                        int countBook = item.getTotalBook();
                        int countMemo = item.getTotalMemo();

                        setCountStyledText(bookCountTxt,
                                "나는 총 %d권의 \n책을 읽었어요",
                                countBook, "권");

                        setCountStyledText(memoCountTxt,
                                "나는 총 %d개의 \n메모를 남겼어요",
                                countMemo, "개");
                    } else {
                        Toast.makeText(requireContext(),
                                "리포트 데이터가 없습니다.",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(requireContext(),
                            "리포트 조회 실패: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {
                Toast.makeText(requireContext(),
                        "onFailure: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void setCountStyledText(TextView tv, String format, int count, String unit) {
        String full = String.format(Locale.KOREA, format, count);
        SpannableString span = new SpannableString(full);

        String numStr = String.valueOf(count);
        int start = full.indexOf(numStr);
        int end = start + numStr.length() + unit.length();

        if (start >= 0) {
            span.setSpan(new RelativeSizeSpan(1.5f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(span);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        v.findViewById(R.id.infoEditBtn).setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_frame, new EditInfoFragment())
                    .addToBackStack("edit_profile")
                    .commit();
        });
    }
}


