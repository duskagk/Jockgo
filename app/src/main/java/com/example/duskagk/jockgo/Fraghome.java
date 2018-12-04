package com.example.duskagk.jockgo;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class Fraghome extends Fragment {
    View view;
    private ArrayList<String> mNames= new ArrayList<>();
    private ArrayList<String> mImages= new ArrayList<>();
    private ArrayList<Integer> tag = new ArrayList<>();
    public Fraghome(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.home_f,container,false);
        getImages();
        Button login=view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
//                AlertDialog.Builder mbul=new AlertDialog.Builder(getContext());
//                View mv=getLayoutInflater().inflate(R.layout.subject_dialog,null);
//                EditText ed=(EditText)mv.findViewById(R.id.q_cnt);
//                mbul.setView(mv);
            }
        });
        return view;
    }

    private void getImages(){

        NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/book", null, "GET");
        try {
            String result = networkTask.execute().get();



            String url = "https://s3.ap-northeast-2.amazonaws.com/jockgo/";
            JSONArray jsonArray = new JSONArray(result);

            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                mImages.add(url + jsonObj.get("b_image").toString() + ".jpg");
                mNames.add(jsonObj.get("b_name").toString());
                tag.add(Integer.parseInt(jsonObj.get("b_no").toString()));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        mImages.add("http://image.yes24.com/momo/TopCate167/MidCate06/16659691.jpg");
//        mNames.add("수학");
////
//        mImages.add("https://image.aladin.co.kr/product/2782/56/cover/8998756161_1.jpg");
//        mNames.add("c언어");
//
//        mImages.add("http://www.hanbit.co.kr/data/books/B8123297462_l.jpg");
//        mNames.add("c++");
//
//        mImages.add("http://www.hanbit.co.kr/data/books/B3450156021_l.jpg");
//        mNames.add("알고리즘");
//
//        mImages.add("http://www.hanbit.co.kr/data/books/B1779572378_l.jpg");
//        mNames.add("그래픽스");
//
//        mImages.add("http://userbook.net/wp/wp-content/uploads/2014/04/L-210x300.jpg");
//        mNames.add("사물인터넷");
//
//        mImages.add("http://image.kyobobook.co.kr/images/book/large/717/l9788973387717.jpg");
//        mNames.add("영상처리");
//
//        mImages.add("http://www.hanbit.co.kr/data/books/B4606522972_l.jpg");
//        mNames.add("기계학습");
//
//        mNames.add("http://bimage.interpark.com/goods_image/5/5/1/9/264105519g.jpg");
//        mNames.add("웹프로그래밍");
        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView:  initrecyclerview");

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.rcview);
        recyclerView.setLayoutManager(layoutManager);
        RecycleAdapter adapter=new RecycleAdapter(getContext(),mNames,mImages, tag);
        recyclerView.setAdapter(adapter);
    }
}
