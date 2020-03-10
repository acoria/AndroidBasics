package com.example.androidbasics.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidbasics.R;
import com.example.androidbasics.pojo.RandomWord;

import java.util.ArrayList;
import java.util.List;

public class DBConnectionAdapter extends RecyclerView.Adapter<DBConnectionAdapter.DBViewHolder> {

    private List<RandomWord> randomWords = new ArrayList<>();

    public DBConnectionAdapter(){}
    public DBConnectionAdapter(List<RandomWord> randomWords) {
        this.randomWords = randomWords;
    }
    @NonNull
    @Override
    public DBViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_dbconnection,null);
        return new DBViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DBViewHolder holder, int position) {
        String text;
        if(randomWords.get(position) == null) {
            text = "invalid data";
        }else{
            text = randomWords.get(position).getWord();
        }
        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        return randomWords.size();
    }

    public void updateData(List<RandomWord> words){
        this.randomWords = words;
        notifyDataSetChanged();
    }

    public class DBViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public DBViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_db);
        }
    }

}
