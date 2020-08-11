package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ContractProductExample;

import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    @Reference
    private FactoryService factoryService;

    /**
     * 分页查询所有的货物
     *      参数：
     *          当前页：page
     *          每页条数：size
     *          购销合同id：contractId
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5")int size,String contractId) {
        //1、根据购销合同的id，查询所有货物
        ContractProductExample example = new ContractProductExample();
        ContractProductExample.Criteria criteria = example.createCriteria();
        criteria.andContractIdEqualTo(contractId);
        PageInfo info = contractProductService.findAll(example, page, size);
        request.setAttribute("page",info);
        //2、为了展示所有的厂家数据下拉框，查询所有的生产货物的厂家
        //根据ctype=“货物”的查询生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryCriteria = factoryExample.createCriteria();
        factoryCriteria.andCtypeEqualTo("货物");
        List<Factory> list = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",list);
        //3.为了保存方便，需要将购销合同id，传入到request域中
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-list";
}
}
