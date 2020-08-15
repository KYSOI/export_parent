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
    private ContractProductDao contractProductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    /**
     * 保存
     * 参数:货物对象
     * 购销合同id:contractId
     * 单价:price
     * 数量:Cnumber
     * 总金额:Amount
     * 购销合同表中字段
     * totalAmount:总金额
     * proNum:货物数量
     * extNum:附件数量
     *
     * @param
     */
    public void save(ContractProduct cp) {
        double money = 0.0;
        //    计算当前货物总金额
        if (cp.getPrice() != null && cp.getCnumber() != null) {
            money = cp.getPrice() * cp.getCnumber();
        }
        cp.setAmount(money);
        //    设置获取id
        cp.setId(UUID.randomUUID().toString());
        //    保存货物到数据库中
        contractProductDao.insertSelective(cp);
        //    根据购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        //    设置合同总金额(原有金额+添加的货物金额
        contract.setTotalAmount(contract.getTotalAmount() + money);
        //    修改合同的货物数量
        contract.setProNum(contract.getExtNum() + 1);
        //    更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    // 更新货物
    public void update(ContractProduct newCp) {
        //    计算更新之后的总金额
        double newMoney = 0.0;
        if (newCp.getPrice() != null && newCp.getCnumber() != null) {
            newMoney = newCp.getPrice() * newCp.getCnumber();
        }
        newCp.setAmount(newMoney);

        //    获取更新之前的总金额(查询数据库)
        ContractProduct oldCp = contractProductDao.selectByPrimaryKey(newCp.getId());
        Double oldMoney = oldCp.getAmount();//总金额

        //    更新货物
        contractProductDao.updateByPrimaryKeySelective(newCp);
        //    根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(newCp.getContractId());
        //    设置购销合同总金额
        contract.setTotalAmount(contract.getTotalAmount() - oldMoney + newMoney);
        //    更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除货物
     * 参数：货物id
     * 附件对象：ExtCproduct ，其中包含一个contractProductId的字段（表示货物id）
     */
    public void delete(String id) {
        //  1、根据货物id查询货物
        ContractProduct cp = contractProductDao.selectByPrimaryKey(id);
        //  根据id删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //   根据货物id查询所有的货物附件
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractIdEqualTo(id);
        List<ExtCproduct> ecps = extCproductDao.selectByExample(example);//所有附件
        //  计算删除的总金额(货物金额+附件金额)
        Double money = cp.getAmount();
        //  循环删除所有的附件
        for (ExtCproduct ecp : ecps) {
            money += ecp.getAmount();
            extCproductDao.deleteByPrimaryKey(ecp.getId());//根据附件id删除附件
        }
        // 根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        // 设置购销合同总金额
        contract.setTotalAmount(contract.getTotalAmount() - money);
        // 设置货物数,附件数
        contract.setProNum(contract.getProNum() - 1);//发货数
        contract.setExtNum(contract.getExtNum() - ecps.size());
        //更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    //根据id查询
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    //分页查询
    public PageInfo findAll(ContractProductExample example, int page, int size) {
        PageHelper.startPage(page, size);
        List<ContractProduct> list = contractProductDao.selectByExample(example);
        return new PageInfo(list);
    }

    /**
     * 批量保存货物
     */
    public void saveAll(List<ContractProduct> list) {
        if (list!=null) {
            for (ContractProduct contractProduct : list) {
                save(contractProduct);
            }
        }

    }
}
