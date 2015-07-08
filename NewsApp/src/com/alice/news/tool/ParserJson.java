package com.alice.news.tool;

import com.alice.news.entity.News;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class ParserJson {
    public static List<News> parserJson(byte[] arr) throws UnsupportedEncodingException, JSONException {
        List<News> list = null;
        String jsonStr = new String(arr, "utf-8");
        JSONObject object = new JSONObject(jsonStr);

        //TODO 解析JSON字符串




        return list;
    }
}
