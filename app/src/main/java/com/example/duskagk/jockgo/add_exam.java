package com.example.duskagk.jockgo;

import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class add_exam extends AppCompatActivity {
    final int REQ_CODE_SELECT_IMAGE=100;


    JSONArray jsonArray;
    String selectSubject;
    /////
    int b_no = 10;
    //////임시
    int odid=0;
    LinearLayout layout=null;
    LinearLayout laybox=null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQ_CODE_SELECT_IMAGE){
            if(resultCode==Activity.RESULT_OK){
                try{

                    Bitmap img_bit=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    ImageView img=(ImageView)findViewById(R.id.seimg);
                    img.setImageBitmap(img_bit);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        String str;
        TextView subname=(TextView)findViewById(R.id.add_sub_name);
        Intent intent = getIntent();
        str=intent.getExtras().getString("Name");
        subname.setText(str);

        Button btnSubject = (Button) findViewById(R.id.btnExamSubject);
        Button btnDenouement = (Button) findViewById(R.id.btnExamDenouement);
        Button btnanswer=(Button)findViewById(R.id.addanswer);
        Button imgbtn=(Button)findViewById(R.id.addimg);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgin=new Intent(Intent.ACTION_PICK);
                imgin.setType(MediaStore.Images.Media.CONTENT_TYPE);
                imgin.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imgin,REQ_CODE_SELECT_IMAGE);
            }
        });





        btnSubject.setOnClickListener(clickBtn("subject"));
        btnDenouement.setOnClickListener(clickBtn("denouement"));
        btnanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(odid<4){
                TextView edt=new TextView(add_exam.this);
                edt.setText("오답");
                String strColor = "#ff0000";
                edt.setTextColor(Color.parseColor(strColor));
                layout=(LinearLayout)findViewById(R.id.answers);
                laybox=(LinearLayout)findViewById(R.id.laybox);
                EditText odap=new EditText(add_exam.this);
                odap.setId(odid);
                odap.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                edt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                    odap.setHint("오답");

                layout.addView(odap);
                laybox.onViewAdded(layout);
                odid++;
                }
            }
        });

    }


    public String getImageToUri(Uri data){
        String[] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
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

                for (int i = 0; i < jsonArray.length(); i++){
                    try {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        if (selectSubject.equals(jsonObj.getString("s_subject"))){
                            adapter.add(jsonObj.getString("s_denouement"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                spinner.setAdapter(adapter);
                //spinner.setOnItemClickListener(selectSpinner());
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

                button.setOnClickListener(clickPush(s, editText));


                dialog.setView(inflateView);
                dialog.create();
                dialog.show();
            }
        };
    }

    private View.OnClickListener clickPush(final String s, final EditText ed){
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
