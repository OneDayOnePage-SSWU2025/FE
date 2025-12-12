package com.example.onedayonepaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onedayonepaper.data.item.HomeGroupItem;
import com.example.onedayonepaper.data.mapper.PetImageMapper;

import java.util.List;

public class HomePagerAdapter extends RecyclerView.Adapter<HomePagerAdapter.ViewHolder> {

    private List<HomeGroupItem> items;

    public HomePagerAdapter(List<HomeGroupItem> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCharacter, ivBackground;
        TextView tvClub, tvRank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCharacter = itemView.findViewById(R.id.ivCharacter);
            ivBackground = itemView.findViewById(R.id.ivBackground);
            tvClub = itemView.findViewById(R.id.tvClub);
            tvRank = itemView.findViewById(R.id.tvRank);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeGroupItem item = items.get(position);

        holder.ivCharacter.setImageResource(PetImageMapper.getCharacterImage(item.getPetType(), item.getTotalBook()));
        holder.ivBackground.setImageResource(R.drawable.bg_room);

        holder.tvClub.setText(item.getGroupName());
        holder.tvRank.setText("상위 " + item.getPercentage() + "%");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

