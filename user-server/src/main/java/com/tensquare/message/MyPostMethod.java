package com.tensquare.message;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/test")
@Controller
public class MyPostMethod {

    /**
     *   这个变量用于装cookies信息
     */
    private static Cookie cookie;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public void login(
            HttpServletResponse response,
            @RequestParam(value = "userName",required = true) String userName,
            @RequestParam(value = "passWord",required = true) String passWord
    ){

//            return "<Html><body>哈哈哈</body></Html>";  需要在方法上加 @ResponseBody

        cookie = new Cookie("login","true");
        response.addCookie(cookie);
        response.setHeader("Access-Control-Expose-Headers","Cache-Control,Content-Type,Expires,Pragma,Content-Language,Last-Modified,token");
        response.setHeader("token", "123456"); //设置响应头
    }


}
