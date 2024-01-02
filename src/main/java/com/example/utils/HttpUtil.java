package com.example.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author qinshixin
 * @Date 2023/10/30 20:02
 * @Version 1.0
 */
public class HttpUtil {

    public static String postXwwFormUrlEncoded(String url, HashMap<String, String> paraMap) throws IOException {
        List<NameValuePair> params = new ArrayList<>();
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String msg = "";
        try {
            if (paraMap != null && paraMap.size() != 0) {
                for (Map.Entry<String, String> entry : paraMap.entrySet()) {
                    params.add(new NameValuePair() {
                        @Override
                        public String getName() {
                            return entry.getKey();
                        }
                        @Override
                        public String getValue() {
                            return entry.getValue();
                        }
                    });
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpURLConnection.HTTP_OK == statusCode) {
                HttpEntity entity = response.getEntity();
                msg = EntityUtils.toString(entity);
            }else {
                return "{\"status\": \"0\",\"desc\": \"接口请求异常\"}";
            }
        } catch (IOException e) {
            throw e;
        }
        return msg;
    }

}
