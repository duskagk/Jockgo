package com.example.duskagk.jockgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StudyRecycle extends RecyclerView.Adapter<StudyRecycle.ViewHolder> {

    private ArrayList<String> mNames= new ArrayList<>();
    private ArrayList<String> mImages= new ArrayList<>();
    private Context mContext;
    private ArrayList<Integer> b_no;
    private int u_no;

    public StudyRecycle(Context mContext,ArrayList<String> mNames, ArrayList<String> mImages, ArrayList<Integer> mTag, int u_no) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mContext = mContext;
        this.b_no = mTag;
        this.u_no = u_no;
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

        NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/info?b_no=" + b_no.get(position) + "&u_no=" + u_no, null, "GET");
        try {
            String tmp = networkTask.execute().get();
            if (tmp == null){
                holder.hit.setText(tmp + "%");
            }
            else {
                holder.hit.setText(tmp + "%");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
        TextView hit;


        public ViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.studyimg );
            record=itemView.findViewById(R.id.studysub);
            hit=itemView.findViewById(R.id.studyHit);
        }
    }
}
