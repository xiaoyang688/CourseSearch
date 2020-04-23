package com.xiaoyang.service;

public interface Answer150sService {

    /**
     * 150s官网接口
     *
     * @param text
     * @return
     */
    String getAnswerByWeb(String text);

    /**
     * 150s付费接口
     * @param text
     * @return
     */
    String getAnswerByToken(String text);

}
