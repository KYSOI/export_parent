package cn.itcast.web.controller.system;


import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/role")
public class RodeController extends BaseController {


    @Autowired
    private RoleService roleService;


    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size) {

        //调用service分页查询
        PageInfo info = roleService.findAll(getLoginCompanyId(), page, size);
        request.setAttribute("page", info);
        return "system/role/role-list";
    }

    //新建跳转
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "system/role/role-add";
    }

    //新建或修改
    @RequestMapping("/edit")
    public String edit(Role role) {
        role.setCompanyId(getLoginCompanyId());
        role.setCompanyName(getLoginCompanyName());
        if (StringUtils.isEmpty(role.getId())) {
            roleService.save(role);
        } else {
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    //    编辑数据回显
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {

        Role role = roleService.findById(id);
        request.setAttribute("role", role);
        return "/system/role/role-update";
    }

//    删除
    @RequestMapping("/delete")
    public String delete(String id) {
        roleService.delete(id);
        return"redirect:/system/role/list.do";
    }
}
