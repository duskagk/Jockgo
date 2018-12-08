package com.example.duskagk.jockgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signUP extends AppCompatActivity {

    EditText mId, mPass1, mPass2, mName;
    Spinner mSchool;
    Button btnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mId = (EditText) findViewById(R.id.id);
        mPass1 = (EditText) findViewById(R.id.pass1);
        mPass2 = (EditText) findViewById(R.id.pass2);
        mName = (EditText) findViewById(R.id.name);

        mSchool = (Spinner) findViewById(R.id.school);
        btnSign = (Button)findViewById(R.id.signup);

        getSchoolSpinner();

        btnSign.setOnClickListener(new btnSignUpListener());
    }
    public static boolean checkEmail(String email){

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }


    public void getSchoolSpinner(){
        NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/school", null, "GET");
        try {
            String result = networkTask.execute().get();



            JSONArray jsonArray=new JSONArray(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item);

            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                adapter.add(jsonObj.get("u_school").toString());
            }

            mSchool.setAdapter(adapter);
            mSchool.setSelection(0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class btnSignUpListener implements Button.OnClickListener{
        @Override
        public void onClick(View v) {

            if (mPass1.getText().toString().equals( mPass2.getText().toString() )&&checkEmail(mId.getText().toString())==true) { //비밀번호 비빌번호 확인 검사
                JSONObject values = new JSONObject();
                try {
                    values.put("id", mId.getText().toString());
                    values.put("pass", mPass1.getText().toString());
                    values.put("name", mName.getText().toString());
                    values.put("school", mSchool.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/register", values, "POST");
                networkTask.execute();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
            else if(checkEmail(mId.getText().toString())==false){
                Toast.makeText(getApplicationContext(),"이메일 형식이 아닙니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
