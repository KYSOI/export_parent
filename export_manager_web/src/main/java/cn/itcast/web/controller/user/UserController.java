package cn.itcast.web.controller.user;

import cn.itcast.dao.user.UserDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.user.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.user.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;


    //删除
    @RequestMapping("/delete")
    public String delete(String id) {
        userService.delete(id);
        return "redirect:/system/user/list.do";
    }
    //    根据企业id保存或修改用户
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        String companyId = getLoginCompanyId();

        User user=userService.findUserById(id);
        List<Dept> list = deptService.findAll(companyId);
        request.setAttribute("user",user);
        request.setAttribute("deptList",list);
        return "system/user/user-update";

    }

    @RequestMapping("/edit")
    public String edit(User user) {
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        //  保存
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);

        if (StringUtils.isEmpty(user.getId())) {
            userService.save(user);
        }else {
            userService.update(user);
        }
        //重定向
        return "redirect:/system/user/list.do";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        String companyId = getLoginCompanyId();
        //下拉框()之前写的复用
        List<Dept>list=deptService.findAll(companyId);
        request.setAttribute("deptList",list);
        return"system/user/user-add";
    }


    //    根据登录人企业id查询用户列表(分页)
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        //    获取登录人企业id
        String companyId = getLoginCompanyId();

        //调用service
        PageInfo<User> info = userService.findAll(companyId, page, size);
        request.setAttribute("page", info);
        //   跳转
        return "system/user/user-list";

    }

}
