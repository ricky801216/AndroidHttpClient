package com.example.apple.androidhttpclient;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by apple on 16/9/26.
 */

public class MyDataHandler extends DefaultHandler {
    boolean flag = false , isItem = false, isLink = false;
    ArrayList<String> data = new ArrayList();
    ArrayList<String> link = new ArrayList();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        // 创建约束条件,只有符合约束的元素才会进入方法,
        // qName应该是元素对象的名称
        if (qName.equals("title")) {
            flag = true;

        }
        if (qName.equals("link")){
            flag = true;
        }
        if (qName.equals("item")){
            flag = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        // 判断操作的标签对象是否符合规范
        if (qName.equals("title")) {

            flag = false;
        }

        if (qName.equals("link")){
            flag = false;
        }
        if (qName.equals("item")){
            flag = false;
        }
    }
    //取出了一個文字
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        // 首先判断控制开关是否打开
        if (flag == true && isItem == true) {
            // 输出元素对象,即两个标签之间所标注的文本内容(相当于DOM解析中的getNodeValue();操作)
            String fetchStr = new String(ch).substring(start,start+length);
            Log.d("XML",fetchStr);
            data.add(fetchStr);
        }

        if (flag == true && isLink == true) {
            // 输出元素对象,即两个标签之间所标注的文本内容(相当于DOM解析中的getNodeValue();操作)
            String fetchStr = new String(ch).substring(start, start + length);
            link.add(fetchStr);
        }
        }
}
