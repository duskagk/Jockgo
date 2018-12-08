package com.example.duskagk.jockgo;

import android.content.Intent;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class add_exam extends AppCompatActivity {


    JSONArray jsonArray;
    String selectSubject;
    ArrayList<EditText> wAnswer = new ArrayList<>();

    //ArrayList<Integer> s_no;
    /////
    int b_no, s_no, u_no;
    //////임시
    int odid=0;
    LinearLayout layout=null;
    LinearLayout laybox=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        String str;
        TextView subname=(TextView)findViewById(R.id.add_sub_name);

        Intent intent = getIntent();
        subname.setText(intent.getExtras().getString("name"));
        b_no = intent.getIntExtra("num", 0);
        MyApplication myApp = (MyApplication)getApplication();
        u_no = myApp.getNo();

        getSpinner();

        Button btnSubject = (Button) findViewById(R.id.btnExamSubject);
        Button btnDenouement = (Button) findViewById(R.id.btnExamDenouement);
        Button btnAnswer=(Button)findViewById(R.id.addanswer);
        Button btnPush=(Button)findViewById(R.id.examPush);






        btnSubject.setOnClickListener(clickBtn("subject"));
        btnDenouement.setOnClickListener(clickBtn("denouement"));
        btnPush.setOnClickListener(clickPush());
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(odid<4){
                TextView edt=new TextView(add_exam.this);
                edt.setText("오답");
                String strColor = "#ff0000";
                edt.setTextColor(Color.parseColor(strColor));
                layout=(LinearLayout)findViewById(R.id.answers);
//                laybox=(LinearLayout)findViewById(R.id.asdfxcv);
                EditText odap=new EditText(add_exam.this);
                wAnswer.add(odap);
                odap.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                edt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(odap);
                layout.addView(edt);
                odid++;
                }
            }
        });

    }

    private View.OnClickListener clickPush() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerSubject = (Spinner)findViewById(R.id.examSubject);
                Spinner spinnerDenouement = (Spinner)findViewById(R.id.examDenouement);

                EditText problemContent = (EditText)findViewById(R.id.add_exam);
                EditText answerContent = (EditText)findViewById(R.id.examAnswer);

                JSONObject values = new JSONObject();

                try {
                    values.put("s_no", s_no);
                    values.put("u_no", u_no);
                    values.put("p_problem", problemContent.getText().toString());
                    for (int i = 1; i <= wAnswer.size(); i++){
                        values.put("a_choice_" + i, wAnswer.get(i-1).getText().toString());
                    }
                    values.put("a_choice_" + (odid+1), answerContent.getText().toString());
                    values.put("a_answer", answerContent.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/problem", values, "POST");
                networkTask.execute();
            }
        };
    }

    private void getSpinner(){
        Spinner spinner = (Spinner)findViewById(R.id.examSubject);
        NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/subject?b_no=" + b_no, null, "GET");
        try {
            String result = networkTask.execute().get();
            Set<String> set = new HashSet<String>();

            jsonArray=new JSONArray(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item);


            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                set.add(jsonObj.getString("s_subject"));
            }

            for (Iterator i = set.iterator(); i.hasNext();){
                adapter.add((String)i.next());
            }



            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(selectSpinner());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemSelectedListener selectSpinner(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)findViewById(R.id.examDenouement);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_spinner_dropdown_item);
                selectSubject = (String) parent.getItemAtPosition(position);

                //s_no = new ArrayList<Integer>();
                for (int i = 0; i < jsonArray.length(); i++){
                    try {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        if (selectSubject.equals(jsonObj.getString("s_subject"))){
                            adapter.add(jsonObj.getString("s_denouement"));

                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(getNum(jsonObj.getInt("s_no")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        };
    }

    private AdapterView.OnItemSelectedListener getNum(final int num){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_no = num;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private View.OnClickListener clickBtn(final String s) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
                final View inflateView = inflater.inflate(R.layout.activity_add_category, null);

                EditText editText = (EditText) inflateView.findViewById(R.id.editTextCategory);
                Button button = (Button)inflateView.findViewById(R.id.btnCategoryPush);

                button.setOnClickListener(clickSpinnerPush(s, editText));


                dialog.setView(inflateView);
                dialog.create();
                dialog.show();
            }
        };
    }

    private View.OnClickListener clickSpinnerPush(final String s, final EditText ed){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject values = new JSONObject();
                if (s.equals("subject")){

                    try {
                        values.put("b_no", b_no);
                        values.put("subject", ed.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } else if (s.equals("denouement")){
                    try {
                        values.put("b_no", b_no);
                        values.put("subject", selectSubject);
                        values.put("denouement", ed.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/subject", values, "POST");
                networkTask.execute();
            }
        };
    }


}
