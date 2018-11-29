package com.example.duskagk.jockgo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StudyRecycle extends RecyclerView.Adapter<StudyRecycle.ViewHolder> {

    private ArrayList<String> mNames= new ArrayList<>();
    private ArrayList<String> mImages= new ArrayList<>();
    private Context mContext;

    public StudyRecycle(Context mContext,ArrayList<String> mNames, ArrayList<String> mImages) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.studylist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(mContext).asBitmap().load(mImages.get(position))
                .into(holder.img);
        holder.record.setText(mNames.get(position));

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,mNames.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView record;

        public ViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.studyimg );
            record=itemView.findViewById(R.id.studysub);
        }
    }
}
