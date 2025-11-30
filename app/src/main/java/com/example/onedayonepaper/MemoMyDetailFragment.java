package com.example.onedayonepaper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.onedayonepaper.data.api.ApiClient;
import com.example.onedayonepaper.data.api.ApiService;
import com.example.onedayonepaper.data.dto.response.BasicResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoMyDetailFragment extends Fragment {

    private ImageView profileImage;
    private TextView memoText;
    private TextView pageText;
    private TextView titleText;

    private int memoId;
    private String bookTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.memo_mydetail, container, false);

        profileImage = view.findViewById(R.id.profilePreview);
        memoText = view.findViewById(R.id.detailMyContent);
        pageText = view.findViewById(R.id.detailMyPage);
        titleText = view.findViewById(R.id.bookTitle);

        Button deleteBtn = view.findViewById(R.id.memoDelBtn);

        Bundle args = getArguments();
        if (args != null) {
            String url = args.getString("profileUrl");
            String memo = args.getString("memo");
            int page = args.getInt("page");
            memoId = args.getInt("memoId");
            bookTitle = args.getString("bookTitle");

            titleText.setText(bookTitle);

            Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .placeholder(R.drawable.bg_circle_green)
                    .error(R.drawable.bg_circle_green)
                    .into(profileImage);

            memoText.setText(memo);
            pageText.setText(page + " p");
        }

        view.findViewById(R.id.backBtn).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        deleteBtn.setOnClickListener(v -> deleteMemo());

        return view;
    }

    private void deleteMemo() {
        ApiService api = ApiClient.getClient(requireContext()).create(ApiService.class);

        api.deleteMemo(memoId).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    Toast.makeText(requireContext(), "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "삭제 실패", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
