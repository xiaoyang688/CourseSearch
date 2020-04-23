package com.xiaoyang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xiaoyang.service.AnswerXueXiaoYiService;
import com.xiaoyang.utils.TokenRandomUtils;
import okhttp3.*;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnswerXueXiaoYiServiceImpl implements AnswerXueXiaoYiService {

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private String URL = "https://app.51xuexiaoyi.com/api/v1/searchQuestion";

    @Override
    public String getAnswer(String text) {


        OkHttpClient client = new OkHttpClient();

        String json = String.format("{\"keyword\":\"%s\"}", text);

        RequestBody body = RequestBody.create(MEDIA_TYPE, json);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("token", TokenRandomUtils.getToken())
                .addHeader("platform", "android")
                .addHeader("app-version", "1.0.5")
                .addHeader("user-agent", "okhttp/3.11.0")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonStr = StringEscapeUtils.unescapeJava(response.body().string());
            Map result = JSON.parseObject(jsonStr, Map.class);
            JSONArray dataArr = (JSONArray) result.get("data");
            Map answerMap = (Map) dataArr.get(0);
            String answer = (String) answerMap.get("a");
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
