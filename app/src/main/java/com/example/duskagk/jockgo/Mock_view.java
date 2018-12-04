package com.example.duskagk.jockgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;

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
    private int mNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_view);
        mNo = getIntent().getIntExtra("no", 0);
        getImages();
    }
    private void getImages(){

        NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/problem?s_no=" + 2 , null, "GET");
        try {
            String result = networkTask.execute().get();

            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                mNames.add(jsonObj.get("p_problem").toString());

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        mNames.add("M이상 N이하의 소수를 모두 출력하는 프로그램을 작성하시오.");
//
//        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\n" +
//                "\n" +
//                "X가 3으로 나누어 떨어지면, 3으로 나눈다.\n" +
//                "X가 2로 나누어 떨어지면, 2로 나눈다.\n" +
//                "1을 뺀다.\n" +
//                "정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");
//
//        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\n" +
//                "\n" +
//                "X가 3으로 나누어 떨어지면, 3으로 나눈다.\n" +
//                "X가 2로 나누어 떨어지면, 2로 나눈다.\n" +
//                "1을 뺀다.\n" +
//                "정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");
//
//        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\\n\" +\n" +
//                "                \"\\n\" +\n" +
//                "                \"X가 3으로 나누어 떨어지면, 3으로 나눈다.\\n\" +\n" +
//                "                \"X가 2로 나누어 떨어지면, 2로 나눈다.\\n\" +\n" +
//                "                \"1을 뺀다.\\n\" +\n" +
//                "                \"정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");
//
//        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\\n\" +\n" +
//                "                \"\\n\" +\n" +
//                "                \"X가 3으로 나누어 떨어지면, 3으로 나눈다.\\n\" +\n" +
//                "                \"X가 2로 나누어 떨어지면, 2로 나눈다.\\n\" +\n" +
//                "                \"1을 뺀다.\\n\" +\n" +
//                "                \"정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");
//
//        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\\n\" +\n" +
//                "                \"\\n\" +\n" +
//                "                \"X가 3으로 나누어 떨어지면, 3으로 나눈다.\\n\" +\n" +
//                "                \"X가 2로 나누어 떨어지면, 2로 나눈다.\\n\" +\n" +
//                "                \"1을 뺀다.\\n\" +\n" +
//                "                \"정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");


        initRecyclerView();
    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView:  initrecyclerview");

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView=findViewById(R.id.mock_paper);
        recyclerView.setLayoutManager(layoutManager);
        MockRecycle adapter=new MockRecycle(this,mNames, this.mAnswer);
        recyclerView.setAdapter(adapter);
    }
}
