package com.example.duskagk.jockgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MockRecycle extends RecyclerView.Adapter<MockRecycle.ViewHolder>{

    private static final String TAG = "RecycleAdapter";

    private ArrayList<String> mNames; //= new ArrayList<>();
    private ArrayList<String> mImages; // = new ArrayList<>();
    private ArrayList<ArrayList<String>> mAnswer; // = new ArrayList<ArrayList<String>>();
    private ArrayList<ViewHolder> VH = new ArrayList<ViewHolder>();
    private ArrayList<Integer> p_no;

    public MockRecycle( ArrayList<String> mNames, ArrayList<ArrayList<String>> mAnswer, ArrayList<Integer> p_no) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mAnswer = mAnswer;
        this.p_no = p_no;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.mock_list,parent,false);
        ViewHolder VH = new ViewHolder(view);
        this.VH.add(VH);
        return VH;
    }

    public int getPNo (int position){
        return VH.get(position).p_no;
    }

    public boolean getAnswer (int position){
        return VH.get(position).checkAnswer();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
//        Glide.with(mContext).asBitmap().load(mImages.get(position))
//                .into(holder.image);

        holder.name.setText(mNames.get(position));
        holder.p_no = p_no.get(position);
        holder.trueAnswer = mAnswer.get(position).get(0);



        for (int i=1; mAnswer.get(position).size() > i; i++){
            CheckBox checkBox = (CheckBox) holder.answer.getChildAt(i);
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setText(mAnswer.get(position).get(i).toString());
        }


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
        ViewGroup answer;
        int p_no;
        String trueAnswer = null;
        public ViewHolder(View itemView) {
            super(itemView);

//            image=itemView.findViewById(R.id.mock_img);
            name=itemView.findViewById(R.id.mock_q);
            answer = (ViewGroup) itemView.findViewById(R.id.mock_answer);
        }

        @SuppressLint("ResourceAsColor")
        public boolean checkAnswer(){

            boolean out = false;
            for (int i = 0; i < 5; i++){
                if (((CheckBox)answer.getChildAt(i)).getText().equals(trueAnswer)){
                    ((CheckBox) answer.getChildAt(i)).setTextColor(R.color.colorMock);
                    if (((CheckBox)answer.getChildAt(i)).isChecked()) {
                        out = true;
                    }
                }
            }
            return out;
        }
    }
}

