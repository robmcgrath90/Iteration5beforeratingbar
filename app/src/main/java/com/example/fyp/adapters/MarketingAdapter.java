package com.example.fyp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.Activities.ViewMarketingComment;
import com.example.fyp.R;
import com.example.fyp.data.model.ModelMarketing;

import java.util.List;

public class MarketingAdapter extends RecyclerView.Adapter<MarketingAdapter.MyViewHolder> {

    //reference https://drive.google.com/file/d/1LTxVNNs-fnD4tIADfqVllZztUFZe3KNl/view
    //reference https://www.youtube.com/watch?v=yPJ_5ybLkFo&list=PLhhNsarqV6MQ-eMvAOwjuBUDm7hfsTUta&index=10
    private ViewMarketingComment activity;
    private List<ModelMarketing> mList;


    public MarketingAdapter(ViewMarketingComment activity , List<ModelMarketing> mList){
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MarketingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item , parent , false);
        return new MarketingAdapter.MyViewHolder(v);



    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.desc.setText(mList.get(position).getDesc());
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title , desc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            desc = itemView.findViewById(R.id.desc_text);
        }
    }

}