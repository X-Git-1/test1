package com.atguigu.servlet;

import com.atguigu.utils.VerifyCodeConfig;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class CodeSenderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Jedis jedis = new Jedis("192.168.43.129",6379);
        //获取手机号
        String phoneNo = request.getParameter("phone_no");
        if (phoneNo==null){
            return;
        }

        /*        3、每个手机号每天只能输入3次*/
        String countKey=VerifyCodeConfig.PHONE_PREFIX+phoneNo+VerifyCodeConfig.COUNT_SUFFIX;
        String count=jedis.get(countKey);
        if (count==null){//第一次发送
            jedis.setex(countKey,VerifyCodeConfig.SECONDS_PER_DAY,"1");
        }else {//不是第一次发送
            int countInt=Integer.parseInt(count);
            if (countInt<3){
                jedis.incr(countKey);
            }else {
                System.out.println("超过3次");
                response.getWriter().write("limit");
                return;
            }
        }



        /*        1、输入手机号，点击发送后随机生成6位数字码，2分钟有效*/
        //生成6位的验证码
        String code = genCode(6);
        System.out.println(code);//向手机发送验证码
/*        jedis.set("code",code);
        jedis.expire("code", 120);*/
        String codeKey= VerifyCodeConfig.PHONE_PREFIX+phoneNo+VerifyCodeConfig.CODE_SUFFIX;
        jedis.setex(codeKey, 120, code);
        response.getWriter().write("true");
    }

    private String genCode(int len) {
        String code = "";
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }



}
