package com.example.demo;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1() throws IOException {
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage("https://www.150s.cn");
        ScriptResult result = page.executeJavaScript("CryptoJS.AES.encrypt('日常运作与项目的区别在于_____________' ,CryptoJS.enc.Hex.parse('39383033327777772e313530732e636e')).ciphertext.toString()");
        String javaScriptResult = (String) result.getJavaScriptResult();
        System.out.println(javaScriptResult);
    }


}
