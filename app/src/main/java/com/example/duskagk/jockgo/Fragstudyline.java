package com.example.duskagk.jockgo;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class Fragstudyline extends Fragment {
    View view;
    private ArrayList<String> mNames= new ArrayList<>();
    private ArrayList<String> mImages= new ArrayList<>();
    private ArrayList<Integer> tag = new ArrayList<>();

    public Fragstudyline(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.studyline_f,container,false);
        getImages();
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

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView:  initrecyclerview");

        MyApplication myApp = (MyApplication)getActivity().getApplication();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.studyrcview);
        recyclerView.setLayoutManager(layoutManager);
        StudyRecycle adapter=new StudyRecycle(getContext(),mNames,mImages, tag, myApp.getNo());
        recyclerView.setAdapter(adapter);
    }

}
