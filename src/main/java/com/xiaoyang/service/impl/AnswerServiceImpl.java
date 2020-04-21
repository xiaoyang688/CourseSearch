package com.xiaoyang.service.impl;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xiaoyang.service.AnswerService;
import com.xiaoyang.utils.UserAgentUtils;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private HtmlPage htmlPage;

    @Autowired
    private WebClient webClient;

    private final String URL_150S = "https://150s.cn/topic/getSubject";
    private final String KEY = "39383033327777772e313530732e636e";
    private final String TOKEN = "";

    @Override
    public String getAnswerByWeb(String text) {
        String url = URL_150S;
        return getResultByWeb(url, text);
    }

    @Override
    public String getAnswerByToken(String text) {
        String url = URL_150S;
        return getResultByToken(url, text, TOKEN);
    }

    /**
     * 通过本地获取secret
     *
     * @return
     */
    public String getSecretByStatic(String text) {
        try {
            webClient.getPage("http://localhost:9090/jm.js");
            String js = "CryptoJS.AES.encrypt('" + text + "' ,CryptoJS.enc.Hex.parse('" + KEY + "')).ciphertext.toString()";
            ScriptResult scriptResult = htmlPage.executeJavaScript(js);
            return (String) scriptResult.getJavaScriptResult();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过官网获取秘钥
     *
     * @param text
     * @return
     */
    public String getSecretByWeb(String text) {
        String js = "CryptoJS.AES.encrypt('" + text + "' ,CryptoJS.enc.Hex.parse('" + KEY + "')).ciphertext.toString()";
        ScriptResult result = htmlPage.executeJavaScript(js);
        return (String) result.getJavaScriptResult();
    }

    /**
     * 获取150s Cookie
     *
     * @return
     */
    private String get150sCookie() {

        Request request = new Request.Builder()
                .url("https://www.150s.cn/")
                .build();

        String cookie = null;
        try (Response response = okHttpClient.newCall(request).execute()) {
            Headers headers = response.headers();
            String[] split = headers.get("Set-Cookie").split(";");
            cookie = split[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookie;
    }

    private String getResultByToken(String url, String text, String token) {

        if (URL_150S.equals(url)) {
            FormBody formBody = new FormBody.Builder()
                    .add("title", text)
                    .add("secret", getSecretByStatic(text))
                    .add("token", token)
                    .build();
            final Request postRequest = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(postRequest).execute();
                String res = response.body().string();
                System.out.println(res);
                Map resMap = JSON.parseObject(res, Map.class);
                String title = (String) resMap.get("title");
                if ("接口未授权".equals(title)) {
                    return getResultByWeb(url, text);
                }
                String result = (String) resMap.get("answer");
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getResultByWeb(String url, String text) {

        String cookie = get150sCookie();

        if (URL_150S.equals(url)) {
            //构造请求体
            FormBody formBody = new FormBody.Builder()
                    .add("title", text)
                    .add("secret", getSecretByStatic(text))
                    .build();
            final Request postRequest = new Request.Builder()
                    .url(url)//请求的url
                    .post(formBody)
                    .addHeader("User-Agent", UserAgentUtils.getUserAgent())
                    .addHeader("Cookie", cookie)
                    .addHeader("accept", "application/json, text/javascript, */*; q=0.01")
                    .addHeader("x-requested-with", "application/x-www-form-urlencoded; charset=UTF-8")
                    .addHeader("origin", "https://150s.cn")
                    .addHeader("referer", "https://150s.cn/")
                    .addHeader("accept-encoding", "gzip, deflate, br")
                    .addHeader("accept-language", "zh-CN,en-US;q=0.8")
                    .build();

            try (Response response = okHttpClient.newCall(postRequest).execute()) {
                String res = response.body().string();
                System.out.println(res);
                Map resMap = JSON.parseObject(res, Map.class);
                String result = (String) resMap.get("answer");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
