package com.xiaoyang.controller;

import com.xiaoyang.service.Answer150sService;
import com.xiaoyang.service.AnswerXueXiaoYiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnswerController {

    @Autowired
    private Answer150sService answer150sService;

    @Autowired
    private AnswerXueXiaoYiService answerXueXiaoYiService;

    @Value("${Search.Type}")
    private String SEARCH_TYPE;

    @GetMapping("/search")
    public String getAnswer(String question) {

        if ("1".equals(SEARCH_TYPE)) {
            return answerXueXiaoYiService.getAnswer(question);
        } else if ("2".equals(SEARCH_TYPE)) {
            return answer150sService.getAnswerByWeb(question);
        } else if ("3".equals(SEARCH_TYPE)) {
            return answer150sService.getAnswerByToken(question);
        }
        return "请在配置文件中配置搜题类型";
    }

}
