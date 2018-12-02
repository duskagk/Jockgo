package com.example.duskagk.jockgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Mock_view extends AppCompatActivity {
    private ArrayList<String> mNames= new ArrayList<>();
    private ArrayList
    private ArrayList<String> mImages= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_view);
        getImages();
    }
    private void getImages(){
        mNames.add("M이상 N이하의 소수를 모두 출력하는 프로그램을 작성하시오.");

        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\n" +
                "\n" +
                "X가 3으로 나누어 떨어지면, 3으로 나눈다.\n" +
                "X가 2로 나누어 떨어지면, 2로 나눈다.\n" +
                "1을 뺀다.\n" +
                "정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");

        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\n" +
                "\n" +
                "X가 3으로 나누어 떨어지면, 3으로 나눈다.\n" +
                "X가 2로 나누어 떨어지면, 2로 나눈다.\n" +
                "1을 뺀다.\n" +
                "정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");

        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"X가 3으로 나누어 떨어지면, 3으로 나눈다.\\n\" +\n" +
                "                \"X가 2로 나누어 떨어지면, 2로 나눈다.\\n\" +\n" +
                "                \"1을 뺀다.\\n\" +\n" +
                "                \"정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");

        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"X가 3으로 나누어 떨어지면, 3으로 나눈다.\\n\" +\n" +
                "                \"X가 2로 나누어 떨어지면, 2로 나눈다.\\n\" +\n" +
                "                \"1을 뺀다.\\n\" +\n" +
                "                \"정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");

        mNames.add("정수 X에 사용할 수 있는 연산은 다음과 같이 세 가지 이다.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"X가 3으로 나누어 떨어지면, 3으로 나눈다.\\n\" +\n" +
                "                \"X가 2로 나누어 떨어지면, 2로 나눈다.\\n\" +\n" +
                "                \"1을 뺀다.\\n\" +\n" +
                "                \"정수 N이 주어졌을 때, 위와 같은 연산 세 개를 적절히 사용해서 1을 만들려고 한다. 연산을 사용하는 횟수의 최솟값을 출력하시오.");


        initRecyclerView();
    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView:  initrecyclerview");

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView=findViewById(R.id.mock_paper);
        recyclerView.setLayoutManager(layoutManager);
        MockRecycle adapter=new MockRecycle(this,mNames);
        recyclerView.setAdapter(adapter);
    }
}
