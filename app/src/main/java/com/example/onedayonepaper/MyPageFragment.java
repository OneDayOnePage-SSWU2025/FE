package com.example.onedayonepaper;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class MyPageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mypage, container, false);

        TextView bookCountTxt = v.findViewById(R.id.bookCountTxt);
        TextView memoCountTxt = v.findViewById(R.id.memoCountTxt);

        // todo: db에 저장된 값으로 대체해야 함
        int countBook = 19;
        int countMemo = 19;

        setCountStyledText(bookCountTxt, "나는 총 %d권의 \n책을 읽었어요", countBook, "권");
        setCountStyledText(memoCountTxt,  "나는 총 %d개의 \n메모를 남겼어요", countMemo, "개");

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

