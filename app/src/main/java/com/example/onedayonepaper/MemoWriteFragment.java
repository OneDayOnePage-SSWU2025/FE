package com.example.onedayonepaper;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.UserInfoResponse;
import com.example.onedayonepaper.data.dto.request.MemoRequest;
import com.example.onedayonepaper.data.dto.response.BookTotalPageResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoWriteFragment extends Fragment {

    private ImageView profilePreview;

    private int bookId;
    private String bookTitle;
    private int totalPageCount = 0;


    private int currentPage = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            bookId = getArguments().getInt("bookId");
            bookTitle = getArguments().getString("bookTitle");


            currentPage = getArguments().getInt("currentPage", -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memo_write, container, false);

        TextView titleText = view.findViewById(R.id.bookTitle);
        titleText.setText(bookTitle);

        EditText memoEdit = view.findViewById(R.id.memoEdit);
        Button memoSendBtn = view.findViewById(R.id.memoSendBtn);

        Button backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        profilePreview = view.findViewById(R.id.profilePreview);

        Glide.with(this)
                .load("https://example.com/user/profile.jpg")
                .placeholder(R.drawable.bg_circle_green)
                .error(R.drawable.bg_circle_green)
                .circleCrop()
                .into(profilePreview);

        loadUserInfo();
        loadTotalPage();


        memoEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                memoSendBtn.setBackgroundResource(
                        s.length() > 0 ? R.drawable.btn_check : R.drawable.btn_check_off
                );
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        memoSendBtn.setOnClickListener(v -> submitMemo(memoEdit));

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
                }
            }

            @Override
            public void onFailure(Call<BookTotalPageResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "페이지 수 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserInfo() {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.getMyInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                    String url = response.body().getData().getImgUrl();

                    Glide.with(MemoWriteFragment.this)
                            .load(url)
                            .placeholder(R.drawable.bg_circle_green)
                            .error(R.drawable.bg_circle_green)
                            .circleCrop()
                            .into(profilePreview);
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "프로필 이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupSpinner(int totalPageCount) {

        List<String> pages = new ArrayList<>();
        for (int i = 1; i <= totalPageCount; i++) {
            pages.add(i + " p");
        }

        LinearLayout spinnerLayout = requireView().findViewById(R.id.pageSpinnerLayout);
        TextView tvSelected = requireView().findViewById(R.id.tvSelectedPage);
        ImageView arrow = requireView().findViewById(R.id.ivArrow);


        int initialPage = (currentPage > 0) ? currentPage : 1;

        tvSelected.setText(initialPage + " p");
        currentPage = initialPage;  // 상태 유지

        ListPopupWindow popup = new ListPopupWindow(requireContext());
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(requireContext(), pages);
        popup.setAdapter(adapter);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setAnchorView(spinnerLayout);
        popup.setModal(true);

        int itemHeight = dpToPx(requireContext(), 48);
        popup.setHeight(itemHeight * 5);
        popup.setWidth(spinnerLayout.getWidth());

        spinnerLayout.setOnClickListener(v -> {
            arrow.animate().rotation(180).setDuration(180).start();
            popup.show();
        });

        popup.setOnDismissListener(() ->
                arrow.animate().rotation(0).setDuration(180).start()
        );

        popup.setOnItemClickListener((parent, view, position, id) -> {
            tvSelected.setText(pages.get(position));
            currentPage = position + 1;
            popup.dismiss();
        });
    }


    private void submitMemo(EditText memoEdit) {

        String memoText = memoEdit.getText().toString();
        if (memoText.isEmpty()) {
            Toast.makeText(requireContext(), "메모를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPage = currentPage;

        MemoRequest request = new MemoRequest(bookId, selectedPage, memoText);

        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);
        api.postMemo(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {

                    MemoSuccessFragment successFragment = new MemoSuccessFragment();

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_frame, successFragment)
                            .commit();

                } else {
                    Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
