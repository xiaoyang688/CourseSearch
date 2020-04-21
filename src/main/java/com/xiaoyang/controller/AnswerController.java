package com.xiaoyang.controller;

import com.xiaoyang.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/search")
    public String getAnswerBy150s(String question) {
        return answerService.getAnswerBy150s(question);
    }

}
