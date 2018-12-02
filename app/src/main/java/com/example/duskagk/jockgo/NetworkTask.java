package com.example.duskagk.jockgo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private JSONObject values;
    private String method;


    public NetworkTask(String url, JSONObject values, String method) {

        this.url = url;
        this.method = method;

        if (method.equals("POST")) {
            this.values = values;}
//        else if (method.equals("GET")) {
//            StringBuffer sbParams = new StringBuffer();
//
//            /**
//             * 1. StringBuffer에 파라미터 연결
//             * */
//            // 보낼 데이터가 없으면 파라미터를 비운다.
//            if (values == null)
//                sbParams.append("");
//                // 보낼 데이터가 있으면 파라미터를 채운다.
//            else {
//                // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
//                boolean isAnd = false;
//                // 파라미터 키와 값.
//                String key;
//                String value;
//
//                for (Iterator<String> name = values.keys(); name.hasNext(); ) {
//                    key = name.next();
//                    value = values.getString(key);
//
//                    // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
//                    if (isAnd)
//                        sbParams.append("&");
//
//                    sbParams.append(key).append("=").append(value);
//
//                    // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
//                    if (!isAnd)
//                        if (_params.size() >= 2)
//                            isAnd = true;
//                }
//            }
//        }

    }

    @Override
    protected String doInBackground(Void... params) {

        String result; // 요청 결과를 저장할 변수.
        HttpProtocol requestHttpURLConnection = new HttpProtocol();
        result = requestHttpURLConnection.request(url, values, method); // 해당 URL로 부터 결과물을 얻어온다.

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
        Log.d("로그", s);
    }
}