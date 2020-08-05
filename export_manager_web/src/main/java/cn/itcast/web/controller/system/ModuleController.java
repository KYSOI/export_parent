package cn.itcast.web.controller.system;


import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import com.google.gson.internal.$Gson$Preconditions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;



    //删除
    @RequestMapping("/delete")
    public String delete(String id) {
        moduleService.delete(id);
        return"redirect:/system/module/list.do";
    }
    //修改跳转
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {

        //回显模块
        Module module=moduleService.findById(id);
        request.setAttribute("module",module);

        List<Module> moduleList = moduleService.findAll();
        request.setAttribute("menus", moduleList);

        return "system/module/module-update";
    }

    //新增修改
    @RequestMapping("/edit")
    public String edit(Module module) {

        if (StringUtils.isEmpty(module.getId())) {
            moduleService.save(module);
        } else {
            moduleService.update(module);
        }

        return "redirect:/system/module/list.do";
    }

    //进入保存页面
    @RequestMapping("/toAdd")
    public String toAdd() {
        //查询出所有模块回显给下拉
        List<Module> moduleList = moduleService.findAll();
        request.setAttribute("menus", moduleList);
        return "system/module/module-add";
    }

    //查询
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        //    分页查询
        PageInfo Info = moduleService.findAll(page, size);
        request.setAttribute("page", Info);
        return "/system/module/module-list";
    }
}
