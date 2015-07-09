package com.alice.news.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 15-7-8.
 * Project: NewsApp
 * User: Alice
 * Data: 15-7-8
 */
public class HttpTools {
    public static byte[] getHttpBytes(String path) throws IOException {
        byte[] arr = null;
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.connect();

        if (con.getResponseCode() == 200){
            InputStream in = con.getInputStream();
            int len;
            byte[] b = new byte[128];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while((len = in.read(b))!= -1){
                bos.write(b, 0, len);
            }
            arr = bos.toByteArray();
        }
        con.disconnect();
        return arr;
    }
}
