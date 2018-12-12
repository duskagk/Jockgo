package com.example.duskagk.jockgo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class add_exam extends AppCompatActivity {
    final int REQ_CODE_SELECT_IMAGE=100;


    JSONArray jsonArray;
    String selectSubject;
    ArrayList<EditText> wAnswer = new ArrayList<>();
    Bitmap img_bit;
    boolean isImage = false;
    //ArrayList<Integer> s_no;
    /////
    int b_no, s_no, u_no;
    //////임시
    int odid=0;
    LinearLayout layout=null;
    LinearLayout laybox=null;

    String returnValue = null;
    String UploadImgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_exam);
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




        btnPush.setOnClickListener(clickPush());
        btnSubject.setOnClickListener(clickBtn("subject"));
        btnDenouement.setOnClickListener(clickBtn("denouement"));
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
                odap.setId(odid);
                odap.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                edt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                    odap.setHint("오답");

                layout.addView(odap);
                layout.addView(edt);
                odid++;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQ_CODE_SELECT_IMAGE){
            if(resultCode==Activity.RESULT_OK){
                try{
                    isImage = true;

                    String[] name_Str = getImageNameToUri(data.getData());
                    Uri selPhotoUri = data.getData();
                    //절대경로 획득**
                    Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
                    c.moveToNext();
                    //String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

                    Bitmap img_bit=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    ImageView img=(ImageView)findViewById(R.id.seimg);

                    //saveBitmaptoJpeg(img_bit, "tmp" ,name_Str[0]);
                    UploadImgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +name_Str[1];
                    img.setImageBitmap(img_bit);

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

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
                if (isImage)
                    imageUpload(UploadImgPath, "jockgo");

                try {
                    values.put("s_no", s_no);
                    values.put("u_no", u_no);
                    values.put("p_problem", problemContent.getText().toString());
                    for (int i = 1; i <= wAnswer.size(); i++){
                        values.put("a_choice_" + (i), wAnswer.get(i-1).getText().toString());
                    }
                    values.put("a_choice_" + (odid+1), answerContent.getText().toString());
                    values.put("a_answer", answerContent.getText().toString());
                    if (isImage && returnValue != null){
                        long now = System.currentTimeMillis();

                        JSONObject returnJson = new JSONArray(returnValue).getJSONObject(0).getJSONObject("data");


                        String MD5 = returnJson.getString("key").split("/")[0];
                        values.put("p_image", MD5);
                    }

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


                dialog.setView(inflateView);
                AlertDialog alertDialog = dialog.create();
                button.setOnClickListener(clickSpinnerPush(s, editText, alertDialog));
                //dialog.create();
                alertDialog.show();


            }
        };
    }

    private View.OnClickListener clickSpinnerPush(final String s, final EditText ed, final AlertDialog alertDialog){
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

                getSpinner();
                alertDialog.dismiss();
            }
        };
    }

    public String[] getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        String path = imgPath.substring(imgPath.lastIndexOf("/0/") + 3);
        String[] str = new String[2];
        str[0] = imgName;
        str[1] = path;
        return str;
    }



    public void imageUpload(final String filename, String stidx) {
        String urlString = "https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/upload";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "--*****";

        try {
            FileInputStream mFileInputStream = new FileInputStream(filename);

            URL connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);

            // open connection
            HttpURLConnection con = (HttpURLConnection) connectUrl.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //dos.writeBytes("Content-Disposition: form-data; name=\"stidx\"\r\n\r\n" + stidx + lineEnd);
            //dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"" + lineEnd);
            //dos.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            dos.writeBytes("Content-Type: image/jpeg\r\n\r\n");

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test", "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = con.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            Log.e("Test", "result = " + s);
            dos.close();

            returnValue = b.toString();


        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            // TODO: handle exception
        }
    }






}
