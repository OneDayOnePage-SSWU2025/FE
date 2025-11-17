package com.example.onedayonepaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyMemoAdapter extends RecyclerView.Adapter<MyMemoAdapter.MemoViewHolder> {

    private final List<MyMemo> memoList;

    public MyMemoAdapter(List<MyMemo> memoList) {
        this.memoList = memoList;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memo, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        MyMemo memo = memoList.get(position);

        holder.memoContent.setText(memo.getContent());
        holder.memoPoint.setText(memo.getPoint());

        Glide.with(holder.itemView.getContext())
                .load(memo.getProfileUrl())
                .circleCrop()
                .into(holder.memoProfile);
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    static class MemoViewHolder extends RecyclerView.ViewHolder {
        ImageView memoProfile;
        TextView memoContent, memoPoint;

        MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            memoProfile = itemView.findViewById(R.id.memoProfile);
            memoContent = itemView.findViewById(R.id.memoContent);
            memoPoint = itemView.findViewById(R.id.memoPoint);
        }
    }
}
