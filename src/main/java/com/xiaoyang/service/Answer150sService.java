package com.xiaoyang.service;

public interface AnswerService {

    /**
     * 150s接口
     *
     * @param text
     * @return
     */
    String getAnswerByWeb(String text);

    String getAnswerByToken(String text);

}
