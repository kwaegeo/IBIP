package com.inside.ibip.global.utils;

import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isGroupNm(String groupNm){
        return Pattern.matches("^[ㄱ-ㅎ|가-힣|0-9|a-z|A-Z|._\\s-]*$", groupNm);
    }

    public static boolean isUserNm(String userNm){
        return Pattern.matches("^[ㄱ-ㅎ|가-힣|0-9|a-z|A-Z|._\\s-]*$", userNm);
    }


    public static boolean isPasswordValid(String password, String userNm) {
        return !hasConsecutiveCharacters(password, 2) &&
                !password.contains(userNm.substring(0, Math.min(2, userNm.length()))) &&
                !password.contains(userNm.substring(Math.max(0, userNm.length() - 2), userNm.length())) &&
                password.length() >= 8 && password.length() <= 20;
    }

    private static boolean hasConsecutiveCharacters(String input, int consecutiveCount) {
        String pattern = "(.)\\1{" + (consecutiveCount - 1) + ",}";
        return Pattern.compile(pattern).matcher(input).find();
    }
}
