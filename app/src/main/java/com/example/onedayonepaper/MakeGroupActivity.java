package com.example.onedayonepaper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MakeGroupActivity extends AppCompatActivity {

    Spinner spinnerTheme;
    TextView tvSpinnerPlaceholder;
    AutoCompleteTextView etSearchBook;
    TextView tvSelectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_makegroup);

        spinnerTheme = findViewById(R.id.spinnerTheme);
        tvSpinnerPlaceholder = findViewById(R.id.tvSpinnerPlaceholder);

        String[] themeList = {
                "에세이/자기계발",
                "과학/IT",
                "시사",
                "사회",
                "고전문학",
                "추리소설",
                "판타지소설",
                "액션/모험",
                "로맨스/멜로",
                "청소년 문학",
                "아동/동화",
                "역사서",
                "시",
                "기타"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_closed_item,
                themeList
        );
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerTheme.setAdapter(adapter);
        spinnerTheme.setPopupBackgroundResource(R.drawable.spinner_popup_bg);

        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }
                String selected = parent.getItemAtPosition(position).toString();
                tvSpinnerPlaceholder.setVisibility(View.GONE);
                Toast.makeText(MakeGroupActivity.this,
                        "선택된 테마: " + selected,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvSpinnerPlaceholder.setVisibility(View.VISIBLE);
            }
        });

        tvSpinnerPlaceholder.setOnClickListener(v -> spinnerTheme.performClick());

        etSearchBook = findViewById(R.id.etSearchBook);
        tvSelectedBook = findViewById(R.id.tvSelectedBook);

        String[] bookList = {
                "Harry 마법사의 돌",
                "Harry 비밀의 방",
                "Harry 아즈카반의 죄수",
                "Harry 불의 잔",
                "Harry 불사조 기사단"
        };

        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner_dropdown,
                bookList
        );

        etSearchBook.setAdapter(bookAdapter);
        etSearchBook.setDropDownBackgroundResource(R.drawable.spinner_popup_bg);
        etSearchBook.setThreshold(1);

        etSearchBook.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBook = parent.getItemAtPosition(position).toString();

            tvSelectedBook.setBackgroundResource(R.drawable.spinner_item_selected);
            tvSelectedBook.setText(selectedBook);
            tvSelectedBook.setTextColor(Color.parseColor("#000000"));

            Toast.makeText(MakeGroupActivity.this,
                    "선택된 도서: " + selectedBook,
                    Toast.LENGTH_SHORT).show();
        });
        etSearchBook.post(() -> {
            int parentWidth = findViewById(R.id.bookSearchContainer).getWidth();
            etSearchBook.setDropDownAnchor(R.id.bookSearchContainer);
            etSearchBook.setDropDownWidth(parentWidth);
            etSearchBook.setDropDownHorizontalOffset(0);
        });
    }
}
