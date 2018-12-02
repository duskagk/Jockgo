package com.example.duskagk.jockgo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MockRecycle extends RecyclerView.Adapter<MockRecycle.ViewHolder>{

    private static final String TAG = "RecycleAdapter";

    private ArrayList<String> mNames= new ArrayList<>();
    private ArrayList<String> mImages= new ArrayList<>();
    private Context mContext;

    public MockRecycle( Context mContext,ArrayList<String> mNames) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.mock_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
//        Glide.with(mContext).asBitmap().load(mImages.get(position))
//                .into(holder.image);

        holder.name.setText(mNames.get(position));
//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked an image:"+mNames.get(position));
//                Toast.makeText(mContext,mNames.get(position),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

//            image=itemView.findViewById(R.id.mock_img);
            name=itemView.findViewById(R.id.mock_q);
        }
    }
}

