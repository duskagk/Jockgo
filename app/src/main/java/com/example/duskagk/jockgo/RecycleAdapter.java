package com.example.duskagk.jockgo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{

    private static final String TAG = "RecycleAdapter";

    private ArrayList<String> mNames= new ArrayList<>();
    private ArrayList<String> mImages= new ArrayList<>();
    private ArrayList<Integer> mTag = new ArrayList<>();
    private Context mContext;

    public RecycleAdapter( Context mContext,ArrayList<String> mNames, ArrayList<String> mImages, ArrayList<Integer> tag) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mContext = mContext;
        this.mTag = tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext).asBitmap().load(mImages.get(position))
                .into(holder.image);

        holder.name.setTag(mTag.get(position));
        holder.name.setText(mNames.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked an image:"+mNames.get(position));

                AlertDialog.Builder mBulid=new AlertDialog.Builder(v.getContext());
                LayoutInflater inf=(LayoutInflater)v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
                View mv=inf.inflate(R.layout.subject_dialog,null);
                EditText q_num=(EditText)mv.findViewById(R.id.q_cnt);
                TextView sub_na=(TextView)mv.findViewById(R.id.subName);
                sub_na.setText(mNames.get(position));
                Button stbtn=(Button)mv.findViewById(R.id.mock_start);
                stbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),Mock_view.class);
                        intent.putExtra("no", mTag.get(position));
                        v.getContext().startActivity(intent);
                    }
                });
                mBulid.setView(mv);
                AlertDialog dialog=mBulid.create();
                dialog.show();
                Toast.makeText(mContext,mNames.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image_view);
            name=itemView.findViewById(R.id.name);
        }
    }
}
