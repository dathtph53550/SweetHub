package com.example.sweethub.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweethub.Model.FeedBack;
import com.example.sweethub.R;

import java.util.ArrayList;

public class AdapterFeedBack extends RecyclerView.Adapter<AdapterFeedBack.FeedBackHolder> {

    Context context;
    ArrayList<FeedBack> list;

    public AdapterFeedBack(Context context, ArrayList<FeedBack> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FeedBackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeedBackHolder(((Activity)context).getLayoutInflater().inflate(R.layout.item_feedback,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedBackHolder holder, int position) {
        FeedBack feedBack = list.get(position);
        holder.tvUser.setText(feedBack.getUser());
        holder.tvNoiDung.setText(feedBack.getNoi_dung());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FeedBackHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvNoiDung;


        public FeedBackHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
        }
    }
}
