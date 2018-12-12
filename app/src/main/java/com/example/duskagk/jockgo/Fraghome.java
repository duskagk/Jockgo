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
import android.widget.TextView;
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

//        MyApplication myApp = (MyApplication)getActivity().getApplication();

        view = inflater.inflate(R.layout.home_f,container,false);
        getImages();
        Button login=view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
//        if (myApp.getLogin()) {
//            TextView textView = (TextView) view.findViewById(R.id.test);
//            textView.setText(myApp.getName());
//        }


        MyApplication myApp = (MyApplication)getActivity().getApplication();
        TextView textView = (TextView)view.findViewById(R.id.test);
        textView.setText(myApp.getName());
        if(myApp.isLogin()){  view.findViewById(R.id.login).setVisibility(View.INVISIBLE); }
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

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.rcview);
        recyclerView.setLayoutManager(layoutManager);
        RecycleAdapter adapter=new RecycleAdapter(getContext(),mNames,mImages, tag);
        recyclerView.setAdapter(adapter);
    }
}
