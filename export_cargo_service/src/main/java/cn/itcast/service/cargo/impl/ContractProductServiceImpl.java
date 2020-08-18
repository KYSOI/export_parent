package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao; //货物dao

    @Autowired
    private ContractDao contractDao; //合同dao

    @Autowired
    private ExtCproductDao extCproductDao; //附件dao

    /**
     * 保存购销合同货物
     *  参数：contractProduct（货物对象）
     *  属性：
     *      contractId:所属的购销合同的id
     *      cnumber：货物数量
     *      price：单价
     *      amount：总金额
     *  购销合同对象：Contract
     *      proNum：货物数量
     *      totalAmount：总金额
     *
     */
    public void save(ContractProduct cp) {
        //1、计算保存的货物总金额
        double money = 0d;
        if(cp.getCnumber() != null && cp.getPrice() != null) {
            money = cp.getCnumber() * cp.getPrice();
        }
        cp.setAmount(money);
        //2、设置货物的id
        cp.setId(UUID.randomUUID().toString());
        //3、调用dao保存货物
        contractProductDao.insertSelective(cp);
        //4、根据合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        //5、设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() + money);
        //6、设置合同的货物数
        contract.setProNum(contract.getProNum() + 1);
        //7、更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 修改货物
     *  参数：
     *      contractProduct（货物对象）
     *      属性：
     *         id：货物id
     *         contractId:所属的购销合同的id
     *         cnumber：货物数量
     *         price：单价
     *         amount：总金额
     *      购销合同对象：Contract
     *         proNum：货物数量
     *         totalAmount：总金额
     */
    public void update(ContractProduct newCp) {
        //1、计算货物更新后的总金额
        double newMoney = 0d;
        if(newCp.getCnumber() != null && newCp.getPrice() != null) {
            newMoney = newCp.getCnumber() * newCp.getPrice();
        }
        newCp.setAmount(newMoney);
        //2、获取货物更新前的总金额
        ContractProduct oldCp = contractProductDao.selectByPrimaryKey(newCp.getId());
        double oldMoney = oldCp.getAmount();
        //3、更新货物
        contractProductDao.updateByPrimaryKeySelective(newCp);
        //4、根据合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(newCp.getContractId());
        //5、更新合同总金额（合同总金额 - 修改之前的金额 + 修改后的金额）
        contract.setTotalAmount(contract.getTotalAmount() - oldMoney + newMoney);
        //6、更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 根据id删除货物
     *  参数:id(货物id)
     *      contractProduct（货物对象）
     *      属性：
     *          id：货物id
     *          contractId:所属的购销合同的id
     *          cnumber：货物数量
     *          price：单价
     *          amount：总金额
     *      购销合同对象：Contract
     *          proNum：货物数量
     *          extNum:附件批次数量
     *          totalAmount：总金额
     *      附件对象: ExtCproduct
     *          cnumber：货物数量
     *          price：单价
     *          amount：总金额
     *          contractProductId:所属货物id
     *
     */
    public void delete(String id) {
        //1、根据货物id查询货物
        ContractProduct cp = contractProductDao.selectByPrimaryKey(id);
        double deleteMoney = cp.getAmount();
        //2、删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //3、根据货物id查询此货物的所有附件
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractProductIdEqualTo(id);
        List<ExtCproduct> ecps = extCproductDao.selectByExample(example);
        //4、循环所有的附件列表
        for (ExtCproduct ecp : ecps) {
            //5、删除每个附件
            extCproductDao.deleteByPrimaryKey(ecp.getId());
            //6、记录删除附件的总金额
            deleteMoney += ecp.getAmount();
        }
        //7、根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        //8、设置购销合同的总金额，附件，货物数
        contract.setTotalAmount(contract.getTotalAmount() - deleteMoney);
        contract.setProNum(contract.getProNum() - 1);//货物数
        contract.setExtNum(contract.getExtNum() - ecps.size());
        //9、更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    //根据id查询
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    //分页查询
    public PageInfo findAll(ContractProductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ContractProduct> list = contractProductDao.selectByExample(example);
        return new PageInfo(list);
    }

    /**
     * 批量保存
     */
    public void saveAll(List<ContractProduct> list) {
        if(list != null) {
            for (ContractProduct contractProduct : list) {
                save(contractProduct);
            }
        }
    }
}
