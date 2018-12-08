package com.example.duskagk.jockgo;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class add_exam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);

        Button btnSubject = (Button) findViewById(R.id.btnExamSubject);
        Button btnDenouement = (Button) findViewById(R.id.btnExamDenouement);

        btnSubject.setOnClickListener(clickBtn("subject"));
        btnDenouement.setOnClickListener(clickBtn("denouement"));
    }

    private void getSpinner(){
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

    private View.OnClickListener clickBtn(final String s) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
                final View inflateView = inflater.inflate(R.layout.activity_add_category, null);

                EditText editText = (EditText) inflateView.findViewById(R.id.editTextCategory);
                Button button = (Button)inflateView.findViewById(R.id.btnCategoryPush);

                button.setOnClickListener(clickPush(s));


                dialog.setView(inflateView);
                dialog.create();
                dialog.show();
            }
        };
    }

    private View.OnClickListener clickPush(final String s){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.equals("subject")){

                } else if (s.equals("denouement")){

                }
            }
        };
    }


}
