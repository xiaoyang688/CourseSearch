package com.xiaoyang.utils;

import java.util.Random;

public class TokenRandomUtils {

    public static String getToken() {
        String[] tokenArr = {"1nc9U7HZej0JMvghh73FPQGukbZkGkieVnyAfMdBzplRGoWAB5yDlJUBkzCW",
                             "hfShuYL03ws6NA2Vum8eCySJyaApBauRFhbwAcDjEaHo9DgLq0ylR1mKoozn"};
        int index = new Random().nextInt(tokenArr.length);
        return tokenArr[index];
    }
}
