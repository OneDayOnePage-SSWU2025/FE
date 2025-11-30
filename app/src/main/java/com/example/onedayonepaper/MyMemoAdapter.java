package com.example.onedayonepaper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onedayonepaper.data.item.MemoItem;

import java.util.List;

public class MyMemoAdapter extends RecyclerView.Adapter<MyMemoAdapter.MemoViewHolder> {

    private final Context context;
    private List<MemoItem> memoList;
    private final int[] bubbleColors;


    public interface OnMemoClickListener {
        void onMemoClick(MemoItem memo);
    }

    private OnMemoClickListener listener;

    public void setOnMemoClickListener(OnMemoClickListener listener) {
        this.listener = listener;
    }

    public MyMemoAdapter(Context context, List<MemoItem> memoList) {
        this.context = context;
        this.memoList = memoList;

        // 컬러 배열 초기화
        this.bubbleColors = new int[]{
                context.getColor(R.color.deepgreen),
                context.getColor(R.color.lightgreen1),
                context.getColor(R.color.lightgreen2),
                context.getColor(R.color.green1)
        };
    }

    public void updateData(List<MemoItem> newList) {
        this.memoList = newList;
        notifyDataSetChanged();
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
        MemoItem item = memoList.get(position);

        holder.memoContent.setText(item.getMemo());
        holder.memoPoint.setText(item.getPage() + "p");

        int randomColor = bubbleColors[position % bubbleColors.length];

        if (holder.itemView.getBackground() != null) {
            holder.itemView.getBackground().setTint(randomColor);
        }

        String imgUrl = item.getImgUrl();
        Log.d("MY_MEMO_ADAPTER", "loading url = " + imgUrl);

        if (imgUrl == null || imgUrl.isEmpty()) {
            holder.memoProfile.setImageResource(R.drawable.bg_circle_green);
        } else {
            Glide.with(context)
                    .load(imgUrl)
                    .circleCrop()
                    .placeholder(R.drawable.bg_circle_green)
                    .error(R.drawable.bg_circle_green)
                    .into(holder.memoProfile);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onMemoClick(item);
        });
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
