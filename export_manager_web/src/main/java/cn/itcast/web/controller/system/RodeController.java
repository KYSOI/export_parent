package cn.itcast.web.controller.system;


import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RodeController extends BaseController {


    @Autowired
    private RoleService roleService;

    @Autowired
    private ModuleService moduleService;


    //实现对角色分配权限
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid, String moduleIds) {
        //调用service
        roleService.updateRoleModule(roleid, moduleIds);
        //重定向
        return "redirect:/system/role/list.do";
    }

    //处理ajax请求
    @RequestMapping("/getZtreeNodes")
    public  @ResponseBody List<Map> getZtreeNodes(String roleId) {
        List<Map> list = new ArrayList<>();
        //1、查询所有的模块 ：List<Module>
        List<Module> modules = moduleService.findAll();
        //2、根据角色id，查询此角色已具有的所有模块id
        List<String> moduleIds = roleService.findModulesByRoleId(roleId);  //1，5，9
        //3、循环所有的模块数据
        for (Module module : modules) {  //所有模块   （1，2，3，4，5，6，7，8，9，10）
            //4、一个Module转化成一个map集合构造节点数据
            Map map = new HashMap();    //9
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
            if(moduleIds.contains(module.getId())) {
                map.put("checked",true);
            }
            list.add(map);
        }
        //5、返回
        return list;
    }

    //角色管理分配,跳转页面
    @RequestMapping("/roleModule")
    public String roleModule(String roleid) {
        Role role = roleService.findById(roleid);
        request.setAttribute("role", role);
        return "system/role/role-module";
    }

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
        return "redirect:/system/role/list.do";
    }
}
