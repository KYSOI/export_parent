package cn.itcast.web.controller.company;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController{

    @Autowired
    private CompanyService companyService;



    @RequestMapping("/list")
    public String findAll() {
        List<Company> list = companyService.findAll();
        request.setAttribute("list", list);
        return "company/company-list";
    }

    /**
     * 进入保存企业页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "company/company-add";
    }

    /**
     * 保存企业
     */
    @RequestMapping("/edit")
    public String edit(Company company) {
        // 判断company对象是否有id
        if (StringUtils.isEmpty(company.getId())) {
            //如果没有保存
            companyService.save(company);
        }else {
            //有就修改跟新
            companyService.update(company);
        }
        return "redirect:/company/list.do";
    }

    /**
     * 企业修改回显
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {

    //    调用service根据id查新企业
       Company company = companyService.findById(id);
    //    将数据存入request
        request.setAttribute("company",company);
    //    页面跳转
        return "company/company-update";
    }


    /**
     * 删除企业
     */
    @RequestMapping("/delete")
    public  String delete(String id) {
        companyService.delete(id);
        return "redirect:/company/list.do";
    }
}
