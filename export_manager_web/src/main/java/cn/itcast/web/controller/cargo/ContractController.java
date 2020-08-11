package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 购销合同的web层（消费者）
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;


    //    分页
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //     创建example
        ContractExample example = new ContractExample();
        //    通过example创建criteria对象
        ContractExample.Criteria criteria = example.createCriteria();
        //    向criteria对象设置条件
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        example.setOrderByClause("create_time DESC");//设置排序
        //    发起调用
        //    设置排序
        PageInfo info = contractService.findAll(page, size, example);
        request.setAttribute("page", info);
        //    跳转页面
        return "cargo/contract/contract-list";
    }


    //   进入保存页面
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }

    //    进入修改页面
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //    根据id查询
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "cargo/contract/contract-update";
    }

    //    保存和修改
    @RequestMapping("/edit")
    public String edit(Contract contract) {
        //设置企业属性
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());
        //判断是否具有id
        if (StringUtils.isEmpty(contract.getId())) {
            contractService.save(contract);
        } else {
            contractService.update(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }

    //    合同提交与取消
    // 跳转页面
    @RequestMapping("/submit")
    public String submit(String id) {
        Contract contract = contractService.findById(id);
        contract.setState(1);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }
    //提交
    @RequestMapping("/cancel")
    public String cancel(String id) {
        Contract contract = contractService.findById(id);
        contract.setState(0);
        contractService.update(contract);
       return "redirect:/cargo/contract/list.do";
    }

    //取消
    @RequestMapping("/delete")
    public String delete(String id) {
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }


}
