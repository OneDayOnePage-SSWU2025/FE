package com.example.onedayonepaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomePagerAdapter extends RecyclerView.Adapter<HomePagerAdapter.ViewHolder> {

    private final List<Integer> characterList;
    private final List<String> clubNames;
    private final List<String> ranks;

    public HomePagerAdapter(List<Integer> characterList, List<String> clubNames, List<String> ranks) {
        this.characterList = characterList;
        this.clubNames = clubNames;
        this.ranks = ranks;
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
        holder.ivCharacter.setImageResource(characterList.get(position));
        holder.ivBackground.setImageResource(R.drawable.bg_room);
        holder.tvClub.setText(clubNames.get(position));
        holder.tvRank.setText(ranks.get(position));
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }
}
