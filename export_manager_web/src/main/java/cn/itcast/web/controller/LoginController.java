package cn.itcast.web.controller;


import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import cn.itcast.util.Encrypt;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController extends BaseController {


    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    //登录
    @RequestMapping("/login")
    public String login(String email, String password) {
        //    判断邮箱和密码是否为空(为空就需要重新登录)
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return "forward:/login.jsp";
        }

        password = Encrypt.md5(password, email);
        //根据邮箱查询用户
        User user = userService.findByEmail(email);
        System.out.println(user.getPassword());
        //判断用户对象是否存在摩玛是否一致
        if (user != null && user.getPassword().equals(password)) {
            //一致登录成功
            session.setAttribute("loginUser",user);

            //登录成功后,查询此用户所有的操作权限
            List<Module> list = moduleService.findByUser(user);
            session.setAttribute("modules",list);
            return "home/main";
        } else {
            //用户不存在或者密码错误(登录失败
            request.setAttribute("error","用户名或密码错误");
            // 跳转到登录页面
            return "forward:/login.jsp";
        }
    }

    //退出
    @RequestMapping(value = "/logout", name = "用户登出")
    public String logout() {
        //SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home() {
        return "home/home";
    }


}
