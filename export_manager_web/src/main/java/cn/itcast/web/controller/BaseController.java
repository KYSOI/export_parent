package cn.itcast.web.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;

    //    获取当前登录用户的企业id
    public String getLoginCompanyId() {
        return "1";
    }

//    获取当前登录用户的企业名称
    public String getLoginCompanyName() {
        return "1";
    }
}
