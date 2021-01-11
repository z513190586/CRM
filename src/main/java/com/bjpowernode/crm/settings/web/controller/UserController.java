package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping("/login.do")
    @ResponseBody
    //返回Object对象（表示数据），框架自动将返回的对象转为json
    public Map login(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        //将密码的明文转为密文
        String loginAct = request.getParameter("loginAct");
        String loginPwd = MD5Util.getMD5(request.getParameter("loginPwd"));
        //接收浏览器端的IP地址
        String ip = request.getRemoteAddr();
        //执行查询操作，查不到就抛异常（自定义异常类,由service类中抛出）
        try {
            User user = service.login(loginAct,loginPwd,ip);
            session.setAttribute("user",user);  //将返回的对象放入域，后面需要使用时方便抓取
            Map<String,Object> map = new HashMap();
            map.put("sucess",true);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            String msgg = e.getMessage();
            Map<String,Object> map = new HashMap<>();
            map.put("sucess",false);
            map.put("msgg",msgg);
            return map;
        }

        //ReturnObject object = new ReturnObject();
        //return object;
    }
}
