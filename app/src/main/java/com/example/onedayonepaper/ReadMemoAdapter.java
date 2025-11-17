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

public class ReadMemoAdapter extends RecyclerView.Adapter<ReadMemoAdapter.MemoViewHolder> {

    private List<ReadMemo> memoList;

    public ReadMemoAdapter(List<ReadMemo> memoList) {
        this.memoList = memoList;
    }

    public void updateData(List<ReadMemo> newList) {
        this.memoList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memo_all, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        ReadMemo memo = memoList.get(position);
        holder.userName.setText(memo.getUserName());
        holder.memoContent.setText(memo.getContent());

        Glide.with(holder.itemView.getContext())
                .load(memo.getProfileUrl())
                .circleCrop()
                .placeholder(R.drawable.bg_circle_green)
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    static class MemoViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName, memoContent;

        MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userName);
            memoContent = itemView.findViewById(R.id.memoContent);
        }
    }
}
