package com.xiaoyang.config;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BeanUtils {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public HtmlPage htmlPage(){
        WebClient webClient = new WebClient();
        try {
            HtmlPage page = webClient.getPage("https://www.150s.cn");
            return page;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
