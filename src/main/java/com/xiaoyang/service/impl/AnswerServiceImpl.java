package com.xiaoyang.service.impl;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.ScriptResult;
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

    private final String URL_150S = "https://150s.cn/topic/getSubject";

    private final String KEY = "39383033327777772e313530732e636e";

    @Override
    public String getAnswerBy150s(String text) {
        String url = URL_150S;
        String cookie = get150sCookie();
        return getResult(url, text, cookie);
    }

    /**
     * 获取秘钥
     *
     * @param text
     * @return
     */
    public String getSecret(String text) {
        System.out.println(text);
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

    private String getResult(String url, String text, String cookie) {
        String result = "null";

        if (URL_150S.equals(url)) {
            //构造请求体
            FormBody formBody = new FormBody.Builder()
                    .add("title", text)
                    .add("secret", getSecret(text))
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
                Map resMap = JSON.parseObject(res, Map.class);
                result = (String) resMap.get("answer");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
