package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;

import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;

    /**
     * 分页查询
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10")  int size) {
        //ContractExample example, int page, int size
        //条件：companyId ,select * from table where company_id=xxx
        //1、实例化example
        ContractExample example = new ContractExample();
        //2、根据example构造criteria对象
        ContractExample.Criteria criteria = example.createCriteria();
        //3、向criteria设置条件
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        example.setOrderByClause("create_time DESC"); //设置排序

        /**
         * 细粒度权限控制
         *  i.获取当前用户的类型
         *  ii.根据不同类型，添加不同的查询条件
         *      普通员工：根据创建人查询（createBy）
         *      部门经理：根据创建人部门查询（createDept）
         */
        User user = getLoginUser();
        Integer degree = user.getDegree();
        if(degree == 4) {  //普通员工
            criteria.andCreateByEqualTo(user.getId());
        }else if(degree ==3 ) {  //部门经理
            criteria.andCreateDeptEqualTo(user.getDeptId());
        }else if(degree ==2) { //上级部门经理 (模糊查询，当前经理的部门id进行like查询)
            criteria.andCreateDeptLike(user.getDeptId()+"%");
        }
        //4、调用方法查询

        PageInfo info = contractService.findAll(page,size,example);

        request.setAttribute("page", info);

        return "cargo/contract/contract-list";
    }


    /**
     * 进入新增页面
     */
    @RequestMapping(value = "/toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }


    //根据id查询
    @RequestMapping(value = "/toUpdate")
    public String toUpdate(String id) {
        //1、调用service根据id查询用户
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);
        //2、页面跳转
        return "cargo/contract/contract-update";
    }

    /**
     * 新增或者修改
     *  参数：contract对象
     *  判断id是否为空，进行保存或者更新
     */
    @RequestMapping(value = "/edit")
    public String edit(Contract contract) {
        //1、设置企业参数
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());
        //2、判断是否具有id
        if(StringUtils.isEmpty(contract.getId())) {
            //在之前的保存案例中，没有对创建人和创建人部门赋值，为了能够完成细粒度权限控制
            //修改保存方法，添加这两个字段

            //获取当前登录用户
            User user = getLoginUser();
            //赋值
            contract.setCreateBy(user.getId()); //创建人ID
            contract.setCreateDept(user.getDeptId()); //创建人所属部门ID

            //2.1 没有id，保存
            contractService.save(contract);
        }else {
            //2.2 有id，更新
            contractService.update(contract);
        }
        //3、重定向到列表
        return "redirect:/cargo/contract/list.do";
    }


    /**
     * 提交
     *  submit
     *  参数：id
     *  业务：
     *    将合同状态由0-->1
     */
    @RequestMapping(value = "/submit")
    public String submit(String id) {
        //1、根据id查询合同
        //2、判断合同的状态是否是0，如果为0（草稿），只有草稿状态的才能更新为1
        //3、更新

        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(1);

        contractService.update(contract);  //根据id非空更新 ： update table set state = 1 where id=#{id}
        //重定向到列表
        return "redirect:/cargo/contract/list.do";
    }


    /**
     * 取消
     *  cancel
     *  参数：id
     *  业务：
     *    将合同状态由1-->0
     */
    @RequestMapping(value = "/cancel")
    public String cancel(String id) {
        //1、根据id查询合同
        //2、判断合同的状态是否是0，如果为0（草稿），只有草稿状态的才能更新为1
        //3、更新

        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);

        contractService.update(contract);  //根据id非空更新 ： update table set state = 1 where id=#{id}
        //重定向到列表
        return "redirect:/cargo/contract/list.do";
    }




}
