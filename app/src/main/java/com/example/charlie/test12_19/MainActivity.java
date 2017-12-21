package com.example.charlie.test12_19;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    protected static final int SUCCESS = 1;
    protected static final int ERROR = 2;

    private EditText et_path;
    private TextView tv_result;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    String text = (String) msg.obj;
                    tv_result.setText(text);
                    break;
                case ERROR:
                    Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_path = (EditText) findViewById(R.id.et_path);
        tv_result = (TextView) findViewById(R.id.tv_result);
    }

    public void click(View view){
        final String path = et_path.getText().toString().trim();
        //访问网络，把html源文件下载下来
        new Thread(){
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");//声明请求方式 默认get
                    //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.3.3; zh-cn; sdk Build/GRI34) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/6.0.0.57_r870003.501 NetType/internet");
                    int code = conn.getResponseCode();
                    if(code ==200){
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readStream(is);

                        Message msg = Message.obtain();
                        msg.obj = result;
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

