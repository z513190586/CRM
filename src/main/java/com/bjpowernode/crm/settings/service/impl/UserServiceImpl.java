package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        //利用map传值给dao层
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = dao.login(map);
        if (user==null){
            throw new LoginException("用户名密码错误");    //抛出自定义异常
        }

        //如果程序成功执行到此处，说明账号密码正确,需要验证其他项

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效");
        }

        //验证锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定，请联系管理员");
        }

        //验证IP地址
        //String allowIps = user.getAllowIps();
        //if (allowIps!=null || allowIps!=""){    //当数据库“允许ip”一栏不为空才进行判断，如果为空就不判断（默认所有ip可访问）
        //    if (!allowIps.contains(ip)){
        //        throw new LoginException("此IP地址无法登陆");
        //    }
        //}
        return user;
    }
}
