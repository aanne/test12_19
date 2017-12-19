package com.example.charlie.test12_19;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button visitWebBtn = null;
    Button downImgBtn = null;
    TextView showTextView = null;
    String resultStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        visitWebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTextView.setVisibility(View.VISIBLE);
                Thread visitBaiduThread = new Thread(new VisitWebRunnable());
                visitBaiduThread.start();
                try {
                    visitBaiduThread.join();
                    if(!resultStr.equals("")){
                        showTextView.setText(resultStr);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public void initUI(){
        showTextView = (TextView)findViewById(R.id.textview_show);
        visitWebBtn = (Button)findViewById(R.id.btn_visit_web);
    }

    private String getURLResponse(String urlString){
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL(urlString); //URL对象
            conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine  = "";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }

        return resultData;
    }

    class VisitWebRunnable implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            resultStr = getURLResponse("http://www.baidu.com/");
        }

    }

}

