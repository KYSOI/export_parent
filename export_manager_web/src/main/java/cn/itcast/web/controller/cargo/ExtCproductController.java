package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.utils.ImageUploadUtils;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    @Reference
    private ExtCproductService extCproductService;

    @Reference
    private FactoryService factoryService;


    /**
     * 分页查询：
     *  根据货物id查询附件，货物和附件（存在货物的id作为外键）是一对多关系
     *  对象：附件ExtcProduct
     *          属性：
     *             contractProductId : 所属货物id
     *  参数：
     *      contractProductId：货物id
     *      contractId:合同id（页面展示）
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10")  int size,
                       String contractProductId,String contractId) {

        //1、查询货物下的所有附件
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractProductIdEqualTo(contractProductId);
        PageInfo info = extCproductService.findAll(example, page, size);
        request.setAttribute("page",info);

        //2、查询所有生产附件的生产厂家
        FactoryExample fe = new FactoryExample();
        FactoryExample.Criteria fc = fe.createCriteria();
        fc.andCtypeEqualTo("附件");
        List<Factory> list = factoryService.findAll(fe);
        request.setAttribute("factoryList",list);

        //3、为了保存方便，记录合同id和货物id
        request.setAttribute("contractProductId",contractProductId);
        request.setAttribute("contractId",contractId);

        return "cargo/extc/extc-list";
    }

    /**
     * 进入到修改页面（根据id查询附件）
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1、根据id查询附件
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct",extCproduct);
        //2、查询所有生产附件的生产厂家
        FactoryExample fe = new FactoryExample();
        FactoryExample.Criteria fc = fe.createCriteria();
        fc.andCtypeEqualTo("附件");
        List<Factory> list = factoryService.findAll(fe);
        request.setAttribute("factoryList",list);
        return "cargo/extc/extc-update";
    }


    /**
     * 新增或者修改附件
     *   参数：附件对象（ExtcProduct）
     *   对象中的属性：
     *      contractId:购销合同id
     *      contractProductId:所属货物的id
     */
    @RequestMapping("/edit")
    public String edit(ExtCproduct ecp) throws IOException {
        //1、设置企业属性
        ecp.setCompanyId(getLoginCompanyId());
        ecp.setCompanyName(getLoginCompanyName());
        //2、判断参数是否具有id
        if(StringUtils.isEmpty(ecp.getId())) {
            //3、没有id，保存
            extCproductService.save(ecp);
        }else {
            //4、有id，更新
            extCproductService.update(ecp);
        }
        //5、重定向
        return "redirect:/cargo/extCproduct/list.do?contractId="+ecp.getContractId()+"&contractProductId="+ecp.getContractProductId();
    }

    /**
     * 删除附件
     *  参数：
     *      id：附件id
     *      contractId：合同id
     *      contractProductId：货物id
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId) throws IOException {
        extCproductService.delete(id);
        //重定向
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }
}
