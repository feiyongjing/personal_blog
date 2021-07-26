package com.feiyongjing.blog.utils;

import javax.servlet.http.HttpServletRequest;

public class AntiReptile {
    public static boolean checkCrawler(HttpServletRequest request){
        return request.getHeader("user-agent") == null || !request.getHeader("user-agent").contains("Mozilla");
    }
}
