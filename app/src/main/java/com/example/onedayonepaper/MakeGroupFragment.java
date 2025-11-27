package com.example.onedayonepaper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

public class MakeGroupFragment extends Fragment {

    Spinner spinnerTheme;
    TextView tvSpinnerPlaceholder;
    AutoCompleteTextView etSearchBook;
    TextView tvSelectedBook;
    EditText etName, etCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.group_makegroup, container, false);

        spinnerTheme = view.findViewById(R.id.spinnerTheme);
        tvSpinnerPlaceholder = view.findViewById(R.id.tvSpinnerPlaceholder);

        etSearchBook = view.findViewById(R.id.etSearchBook);
        tvSelectedBook = view.findViewById(R.id.tvSelectedBook);

        etName = view.findViewById(R.id.etName);
        etCode = view.findViewById(R.id.etCode);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack()
        );

        setupThemeSpinner();
        setupBookSearch(view);

        return view;
    }

    private void setupThemeSpinner() {

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
                requireContext(),
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

                Toast.makeText(requireContext(),
                        "선택된 테마: " + selected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvSpinnerPlaceholder.setVisibility(View.VISIBLE);
            }
        });

        tvSpinnerPlaceholder.setOnClickListener(v -> spinnerTheme.performClick());
    }

    private void setupBookSearch(View view) {

        String[] bookList = {
                "Harry 마법사의 돌",
                "Harry 비밀의 방",
                "Harry 아즈카반의 죄수",
                "Harry 불의 잔",
                "Harry 불사조 기사단"
        };

        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_spinner_dropdown,
                bookList
        );

        etSearchBook.setAdapter(bookAdapter);
        etSearchBook.setDropDownBackgroundResource(R.drawable.spinner_popup_bg);
        etSearchBook.setThreshold(1);

        etSearchBook.setOnItemClickListener((parent, view2, position, id) -> {

            String selectedBook = parent.getItemAtPosition(position).toString();

            tvSelectedBook.setBackgroundResource(R.drawable.spinner_item_selected);
            tvSelectedBook.setText(selectedBook);
            tvSelectedBook.setTextColor(Color.parseColor("#000000"));

            Toast.makeText(requireContext(),
                    "선택된 도서: " + selectedBook,
                    Toast.LENGTH_SHORT).show();
        });

        etSearchBook.post(() -> {
            LinearLayout parentContainer = view.findViewById(R.id.bookSearchContainer);
            int parentWidth = parentContainer.getWidth();

            etSearchBook.setDropDownAnchor(R.id.bookSearchContainer);
            etSearchBook.setDropDownWidth(parentWidth);
            etSearchBook.setDropDownHorizontalOffset(0);
        });
    }
}
