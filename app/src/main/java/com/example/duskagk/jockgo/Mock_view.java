package com.example.duskagk.jockgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class Mock_view extends AppCompatActivity {
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<ArrayList<String>> mAnswer = new ArrayList<ArrayList<String>>();
    private ArrayList<String> mImages= new ArrayList<>();
    private ArrayList<Integer> p_no = new ArrayList<>();
    private int[] mNo;
    private int b_no;
    private MockRecycle adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_view);
        mNo = getIntent().getIntArrayExtra("no");
        b_no = getIntent().getIntExtra("b_no", 0);
        for (int i = 0; i<mNo.length; i++){
            getImages(mNo[i]);
        }

        Button btnPush = (Button)findViewById(R.id.mockPush);
        btnPush.setOnClickListener(clickPush());

        initRecyclerView();
    }

    private View.OnClickListener clickPush(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mNames.size();

                MyApplication myApp = (MyApplication)getApplication();
                int u_no = myApp.getNo();
                if (u_no > 0) {
                    for (int i = 0; size > i; i++) {
                        JSONObject values = new JSONObject();

                        try {
                            values.put("u_no", u_no);
                            values.put("p_no", adapter.getPNo(i));
                            values.put("b_no", b_no);
                            values.put("check", adapter.getAnswer(i) ? "Y" : "N");

                            NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/info", values, "POST");
                            networkTask.execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }


            }
        };
    }

    private void getImages(int s_no){
        NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/problem?s_no=" + s_no , null, "GET");
        try {
            String result = networkTask.execute().get();

            JSONArray jsonArray = new JSONArray(result);


            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    mNames.add(jsonObj.get("p_problem").toString());
                    p_no.add(jsonObj.getInt("p_no"));

                    ArrayList<String> tmpAnswer = new ArrayList<String>();
                    if (jsonObj.get("a_answer").toString().length() > 0)
                        tmpAnswer.add(jsonObj.get("a_answer").toString());
                    if (jsonObj.get("a_choice_1").toString().length() > 0)
                        tmpAnswer.add(jsonObj.get("a_choice_1").toString());
                    if (jsonObj.get("a_choice_2").toString().length() > 0)
                        tmpAnswer.add(jsonObj.get("a_choice_2").toString());
                    if (jsonObj.get("a_choice_3").toString().length() > 0)
                        tmpAnswer.add(jsonObj.get("a_choice_3").toString());
                    if (jsonObj.get("a_choice_4").toString().length() > 0)
                        tmpAnswer.add(jsonObj.get("a_choice_4").toString());
                    if (jsonObj.get("a_choice_5").toString().length() > 0)
                        tmpAnswer.add(jsonObj.get("a_choice_5").toString());
                    mAnswer.add(tmpAnswer);


                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView:  initrecyclerview");

        if (mAnswer.size() > 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            RecyclerView recyclerView = findViewById(R.id.mock_paper);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MockRecycle( mNames, this.mAnswer, p_no);
            recyclerView.setAdapter(adapter);
        }else{
            onBackPressed();
        }
    }
}
