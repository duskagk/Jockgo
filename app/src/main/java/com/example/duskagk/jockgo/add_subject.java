package com.example.duskagk.jockgo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class add_subject extends AppCompatActivity {
    final int REQ_CODE_SELECT_IMAGE=100;


    boolean isImage = false;
    String UploadImgPath;
    String returnValue = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        Button btnPush=(Button)findViewById(R.id.subjectPush);
        btnPush.setOnClickListener(clickPush());
        Button imgbtn=(Button)findViewById(R.id.subjectImage);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgin=new Intent(Intent.ACTION_PICK);
                imgin.setType(MediaStore.Images.Media.CONTENT_TYPE);
                imgin.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imgin,REQ_CODE_SELECT_IMAGE);
            }
        });
    }

    private View.OnClickListener clickPush() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    public void run(){
                        imageUpload(UploadImgPath, "jockgo");

                        EditText book = (EditText)findViewById(R.id.subjectBookName);

                        JSONObject values = new JSONObject();
                        if (isImage)


                            try {

                                values.put("b_name", book.getText().toString());
                                if (isImage && returnValue != null){
                                    JSONObject returnJson = new JSONArray(returnValue).getJSONObject(0).getJSONObject("data");


                                    String MD5 = getMD5(book.getText().toString());
                                    values.put("b_image", MD5);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/book", values, "POST");
                        networkTask.execute();
                    }
                }.start();

            }
        };
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

                    //saveBitmaptoJpeg(img_bit, "tmp" ,name_Str[0]);
                    UploadImgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +name_Str[1];

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

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
        String urlString = "https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/bookupload";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "--*****";

        EditText book = (EditText)findViewById(R.id.subjectBookName);

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
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + getMD5(book.getText().toString()) + ".jpg" + "\"" + lineEnd);
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

    public String getMD5(String str){
        String MD5 = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }
}
