package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public  String delete(String id) {
    //    调用service中delete
        deptService.delete(id);
        //重定向
        return "redirect:/system/dept/list.dao";
    }

    /**
     * 修改部门toUpdate
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
    //根据id查询部门
        Dept dept=deptService.findById(id);
        //    存入request
        request.setAttribute("dept",dept);
        //    查询所有部门下的构造()下拉框
        String companyId = getLoginCompanyId();
        List<Dept> list = deptService.findAll(companyId);
        //将部门id存入request
        request.setAttribute("deptList",list);
        return "system/dept/dept-update";
    }

    /**
     *保存用户/system/dept/toAdd.do
     */

    @RequestMapping("/toAdd")
    public String toAdd() {
    //    查询所有部门(为了构造下拉框,不分页)
        String companyId = getLoginCompanyId();
        List<Dept> list = deptService.findAll(companyId);
        //    部门列表存入request
        request.setAttribute("deptList",list);
        return "system/dept/dept-add";
    //    跳转到页面
    }
//    请求地址/system/dept/list.dao
//    功能:分页查询部门列表
//    问题:查询所有部门还是查询当前登录用户所属企业的部门

    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        //    获取企业id(模拟)
        String companyId = getLoginCompanyId();
        //    调用service分页查询企业列表
        PageInfo info = deptService.findAll(companyId,page,size);
        request.setAttribute("page", info);
        return "system/dept/dept-list";
    }

    /**
     * 保存
     * @param dept
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Dept dept) {
    //    设置部门的企业参数(当前登录人的企业id和名称)
        dept.setCompanyId(getLoginCompanyId());
        dept.setCompanyName(getLoginCompanyName());
    //    判断dept对象是否包含id
        if (StringUtils.isEmpty(dept.getId())) {
        //    如果存在就是保存
            deptService.save(dept);
        }else {
        //    修改
            deptService.update(dept);
        }
        //重定向到列表
        return "redirect:/system/dept/list.do";
    }

}
