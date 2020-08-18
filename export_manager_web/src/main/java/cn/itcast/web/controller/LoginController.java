package cn.itcast.web.controller;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    //基于shiro的用户认证
    @RequestMapping("/login")
    public String login(String email, String password) {
        //判断,如果邮箱或者密码错误重新登录
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return "forward:/login.jsp";
        }
        try {
            // 获取subject工具类
            Subject subject = SecurityUtils.getSubject();
            // 调用subject的login方法进入shiro登录
            UsernamePasswordToken upToken = new UsernamePasswordToken(email, password);
            subject.login(upToken);
            //  如果正常执行无异常,登录成功
            //  从shiro中获取安全数据,登录成功
            User user = (User) subject.getPrincipal();
            //  将对象存入session
            session.setAttribute("loginUser", user);
            //  将用户对象和模块列表对象存入session
            List<Module> list = moduleService.findByUser(user);
            session.setAttribute("modules", list);
            //  跳转主页
            return "home/main";
        } catch (Exception e) {
            e.printStackTrace();
            //    如果判处异常,登录失败(用户名或者密码错误)
            request.setAttribute("error", "用户名或密码错误");
            return "forward:/login.jsp";
        }
    }



    //退出
    @RequestMapping(value = "/logout", name = "用户登出")
    public String logout() {
        //获取subject工具类
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home() {
        return "home/home";
    }
}
