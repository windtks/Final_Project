package com.example.windkts.final_project;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class TranslateActivity extends AppCompatActivity {
    private EditText input;
    private TextView translate_result;
    private TextView web_result;
    private TextView basic_result;
    private ImageButton star;
    private ProgressBar pb;
    private Button s_l;
    private boolean is_basic = true;
    private boolean is_web = true;
    private String translation ="";
    private String web_trans = "";
    private String basic_trans ="";

    String appKey ="7e69071cb0e80746";
    String query = "";
    String salt = String.valueOf(System.currentTimeMillis());
    String from = "";
    String to = "";
    String sign = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);


        getInfo();

        //Init View START
        input = findViewById(R.id.editText);
        translate_result = findViewById(R.id.translate);
        basic_result = findViewById(R.id.basic);
        web_result = findViewById(R.id.web);
        star = findViewById(R.id.imageButton2);
        s_l = findViewById(R.id.source_lan);
        pb = findViewById(R.id.progressBar);
        //Init View END
        final Handler updatehandler = new Handler();
        input.setText(query);
        pb.setVisibility(View.GONE);
        translation ="";
        web_trans = "";
        basic_trans ="";

        //Query Starts
        final Map params = new HashMap();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("sign", sign);
        params.put("salt", salt);
        params.put("appKey", appKey);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //warning: changing UI should only be done in Main Thread!
                    JSONArray JSONtranslation = null;
                    JSONArray JSONexp = null;
                    Log.d("lhl","new thread!");
                    JSONObject result = new JSONObject(readParse(getUrlWithQueryString("http://openapi.youdao.com/api",params)));
                    Log.d("lhl",result.toString());
                    //To do: JSON to String!

                    //GET Translation
                    try{
                        JSONtranslation = new JSONArray(result.getString("translation"));
                        Log.e("lhl","got!");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        for(int i = 0; i < JSONtranslation.length(); i++){
                            translation += JSONtranslation.getString(i);
                            Log.d("lhl",String.valueOf(JSONtranslation.length()));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //GET Translation End
                    //GET BASIC
                    try{
                        JSONObject jsonBasic = (JSONObject) result.get("basic");
                        is_basic = true;
                        JSONexp = new JSONArray(jsonBasic.getString("explains"));
                    }catch (Exception e){
                        is_basic = false;
                    }
                    try{
                        for(int i = 1; i <= JSONexp.length(); i++){
                            basic_trans += "("+i+")"+" "+JSONexp.getString(i)+"\n";
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //GET BASIC END
                    //GET WEB START
                    try{
                        JSONArray JSONweb = result.getJSONArray("web");
                        for(int i = 0 ;i< JSONweb.length(); i++){
                            JSONObject JSONwebson = (JSONObject) JSONweb.get(i);
                            web_trans += JSONwebson.getString("key")+" :\n";
                            JSONArray JSONwebsonson = new JSONArray(
                                    JSONwebson.getString("value"));
                            for(int j = 0; j < JSONwebsonson.length(); j++){
                                web_trans += JSONwebsonson.get(j);
                                if(i != JSONwebsonson.length() - 1) web_trans += ", ";
                            }
                            web_trans += ";\n";
                        }
                        is_web = true;
                    }catch (Exception e){
                        is_web = false;
                        e.printStackTrace();
                    }
                    //GET WEB END
                    Log.d("lhl","prepost");
                    TUpdateUi updateUi = new TUpdateUi(translation,web_trans,basic_trans);
                    Log.d("lhl","post");
                    updatehandler.post(updateUi);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        //Query ends


    }

    private void getInfo(){
        Intent intent = getIntent();
        query = intent.getStringExtra("query");
        from = intent.getStringExtra("source");
        to = intent.getStringExtra("target");
        sign = md5(appKey + query + salt+ "j8saelWS6ebet7gzHGI9z17my2vQ38Wk");
    }

    private class TUpdateUi implements Runnable{
        private String tran;
        private String web;
        private String basic;

        TUpdateUi(String tran,String web,String basic){
            this.tran = tran;
            this.web = web;
            this.basic = basic;
        }

        @Override
        public void run() {
            //更新UI
            translate_result.setText(tran);
            basic_result.setText(basic);
            web_result.setText(web);
        }
    }
    /**
     * 从指定的URL中获取文本
     * @param urlPath
     * @return
     * @throws Exception
     */
    public String readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL(urlPath);
        InputStream inStream = null;
        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            inStream = conn.getInputStream();
        }catch (Exception e){
            Toast.makeText(this,"Timeout",Toast.LENGTH_SHORT).show();
            return "TIMEOUT";
        }
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());
        //通过out.Stream.toByteArray获取到写的数据
    }
    /**
     * 生成32位MD5摘要
     * @param string
     * @return
     */
    public static String md5(String string) {
        if(string == null){
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try{
            byte[] btInput = string.getBytes("utf-8");
            /** 获得MD5摘要算法的 MessageDigest 对象 */
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            /** 使用指定的字节更新摘要 */
            mdInst.update(btInput);
            /** 获得密文 */
            byte[] md = mdInst.digest();
            /** 把密文转换成十六进制的字符串形式 */
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }catch(NoSuchAlgorithmException | UnsupportedEncodingException e){
            return null;
        }
    }
    /**
     * 根据api地址和参数生成请求URL
     * @param url
     * @param params
     * @return
     */
    public static String getUrlWithQueryString(String url, Map params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (Object key : params.keySet()) {
            String value = params.get(key).toString();
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }
    /**
     * 进行URL编码
     * @param input
     * @return
     */
    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

}

