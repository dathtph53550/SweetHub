package com.example.sweethub.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweethub.R;

import java.util.List;

public class SlideshowAdapter extends RecyclerView.Adapter<SlideshowAdapter.SlideshowViewHolder> {

    private List<Integer> images;

    public SlideshowAdapter(List<Integer> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public SlideshowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideshow, parent, false);
        return new SlideshowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideshowViewHolder holder, int position) {
        holder.ivSlideshowImage.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class SlideshowViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlideshowImage;

        public SlideshowViewHolder(View itemView) {
            super(itemView);
            ivSlideshowImage = itemView.findViewById(R.id.ivSlideshowImage);
        }
    }
}
