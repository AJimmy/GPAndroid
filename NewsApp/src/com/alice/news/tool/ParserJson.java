package com.alice.news.tool;

import com.alice.news.entity.News;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
        if ("ok".equals(object.getString("status"))){
            list = new ArrayList<>();
            JSONObject paramz = object.getJSONObject("paramz");
            JSONArray feeds =  paramz.getJSONArray("feeds");
            for (int i = 0; i < feeds.length(); i++) {
                JSONObject dataObj = feeds.getJSONObject(i);
                JSONObject dataObject = dataObj.getJSONObject("data");
                String subject = dataObject.getString("subject");
                String summary = dataObject.getString("summary");
                String cover = dataObject.getString("cover");
                String changed = dataObject.getString("changed");
                list.add(new News(summary, subject,cover,changed));
            }
        }
        return list;
    }
}
