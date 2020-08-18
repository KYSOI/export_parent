package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/export/")
public class ExportController extends BaseController {

    @Reference
    private ContractService contractService;

    @Reference
    private ExportService exportService; //报运单

    @Reference
    private ExportProductService exportProductService; //报运单商品

    /**
     * 查询所有状态=1的购销合同
     */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10")  int size) {
        //1、实例化example
        ContractExample example = new ContractExample();
        //2、根据example构造criteria对象
        ContractExample.Criteria criteria = example.createCriteria();
        //3、向criteria设置条件
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        //查询状态（state）=1
        criteria.andStateEqualTo(1);
        example.setOrderByClause("create_time DESC"); //设置排序
        //4、调用方法查询
        PageInfo info = contractService.findAll(page, size, example);
        request.setAttribute("page", info);

        return "cargo/export/export-contractList";
    }

    /**
     * 分页查询报运单列表
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10")  int size) {
        ExportExample example = new ExportExample();
        ExportExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        PageInfo info = exportService.findAll(example, page, size);
        request.setAttribute("page", info);
        return "cargo/export/export-list";
    }

    /**
     * 进入到新增页面
     *  /cargo/export/toExport.do
     * 参数：
     *    同名参数id，多个id
     *      通过springmvc可以将多个id，自动转化为数组 ： String [] id
     *      在web工程中，如果有多个同名参数请求，自动的转化为字符串（多个参数间通过“，”隔开）
     * 业务逻辑
     *    1、将多个id，转化为字符串，多个id间通过“，”隔开。存入request即可
     *    2、页面跳转
     */
    @RequestMapping("/toExport")
    public String toExport(String id) {
        request.setAttribute("id",id);
        return "cargo/export/export-toExport";
    }

    /**
     * 新增或者修改报运单
     *  参数  export （报运单）
     */
    @RequestMapping("/edit")
    public String edit(Export export) {
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());

        if(StringUtils.isEmpty(export.getId())) {
            exportService.save(export);
        }else{
            exportService.update(export);
        }
        return "redirect:/cargo/export/list.do";
    }


    /**
     * 进入到修改页面
     *  参数：id（报运单ID）
     *  业务：
     *      1、根据报运单id查询报运单
     *         Export
     *      2、根据报运单id查询报运单下的所有商品
     *         ExportProduct
     *           * 外键（exportId）
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1、根据报运单id查询报运单
        Export export = exportService.findById(id);
        request.setAttribute("export",export);
        //2、根据报运单id查询报运单下的所有商品
        ExportProductExample example = new ExportProductExample();
        ExportProductExample.Criteria criteria = example.createCriteria();
        criteria.andExportIdEqualTo(id);
        List<ExportProduct> eps = exportProductService.findAll(example);
        request.setAttribute("eps",eps);
        return "cargo/export/export-update";
    }

    /**
     * 海关保运方法
     *  参数：id（报运单id）
     *  业务：调用service
     */
    @RequestMapping("/exportE")
    public String exportE(String id) {
        exportService.exportE(id);
        return  "redirect:/cargo/export/list.do";
    }

}
