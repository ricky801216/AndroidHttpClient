package com.example.apple.androidhttpclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    MyDataHandler dataHandler;
    ListView lv;
    ArrayAdapter<String> adapter;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.listView);
        //提示要點 ： 網路應用程式不能在主程序上執行,會造成ＵＸ體驗不當
        new Thread() {
            @Override
            public void run() {
                super.run();
                final InputStream inputStream;
                URL url = null;
                try {
                    //題外知識 網頁運作原理： 他會發出ＧＥＴ需求先取得HTML擋並解析
                    //透過url呼出
                     url = new URL("http://udn.com/news/story/10411/1984781");
                    //有此物件 連接你要的網址取得String
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //設定connection物件裡設定ＧＥＴ取得回應
                    conn.setRequestMethod("GET");

                    conn.connect();
                    //得到一個inputStream
                    inputStream = conn.getInputStream();

                    //conn.getContentLength();//可以取得連線後共有多少東西
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                                    //把串流進來的陣列塞進read(buffer) 1024 -> 1036 1024->12 1024 -> -1
                    while ((length = inputStream.read(buffer)) != -1) {
                        //（讀近來, 第0項, 1024）
                        result.write(buffer, 0, length);
                    }
                    //讀取UTF-8編碼
                    String xmlStr = result.toString("UTF-8");
                    Log.d("NET", xmlStr);

                    //利用SAXParserFactory來製造出新的物件讀取xml
                    SAXParserFactory spf = SAXParserFactory.newInstance().newInstance();
                    SAXParser sp = spf.newSAXParser();
                    /*產生新物件*/
                    XMLReader xr = sp.getXMLReader();
                    xr.setContentHandler(dataHandler);
                    /*解析XML*/
                    xr.parse(new InputSource(new StringReader(xmlStr)));


                    //修改主執行緒讓listview可以出來
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_activated_2,dataHandler.data);
                          lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                  Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                intent.putExtra("link", dataHandler.link.get(position));
                                  startActivity(intent);
                              }
                          });
                            lv.setAdapter(adapter);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (ProtocolException e){
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }
}
