package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @creat time: 2018年12月02日 10:06
 * @author: MRT
 * @description
 **/
@RestController
@RequestMapping("/login")
public class LoginController {
    /*
    * @Description:返回当前登陆人的姓名
    * @param: []
    * @return: java.util.Map
    * @author: TanZhiTong
    * @Date: 2018/12/2
    */
    @RequestMapping("/name")
    public Map loginName(){
        String name=SecurityContextHolder.getContext().getAuthentication().getName();
        Map map=new HashMap();
        map.put("loginName",name);
        return map;
    }
}
