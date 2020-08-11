package cn.itcast.controller;


import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {
    /**
     * 调用dubbo服务,@Reference
     */
    @Reference
    private CompanyService companyService;
    /**
     * 企业申请,通过调用服务提供者,将企业数据保存到数据库中
     */
    @RequestMapping("apply")
    @ResponseBody
    public String apply(Company company){
        try {
            companyService.save(company);
            //返回1表示保存成功
            return "1";

        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }
}
