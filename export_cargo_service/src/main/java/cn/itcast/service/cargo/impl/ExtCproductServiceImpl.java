package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * 附件service实现类
 */
@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;//附件dao

    @Autowired
    private ContractDao contractDao; //合同dao


    /**
     * 保存附件
     *  参数：ExtCproduct（附件对象）
     *  属性：
     *      contractId:购销合同id
     *      contractProductId:所属货物id
     *      cnumber：数量
     *      price：单价
     *      amount：总金额
     *  关联对象： contract（合同对象）
     *      totalamount：合同总金额（货物金额+附件金额）
     *      extnum：附件数量
     */
    public void save(ExtCproduct ep) {
        //1、计算附件的总金额
        double money = 0d;
        if(ep.getCnumber() != null && ep.getPrice() != null) {
            money =  ep.getCnumber() * ep.getPrice();
        }
        ep.setAmount(money);
        //2、设置附件的ID
        ep.setId(UUID.randomUUID().toString());
        //3、保存附件
        extCproductDao.insertSelective(ep);
        //4、根据合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(ep.getContractId());
        //5、设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() + money);
        //6、设置合同的附件数
        contract.setExtNum(contract.getExtNum() + 1);
        //7、更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }


    /**
     * 更新附件
     *      参数：ExtCproduct（附件对象）
     *      属性：
     *          contractId:购销合同id
     *          contractProductId:所属货物id
     *          cnumber：数量
     *          price：单价
     *          amount：总金额
     *      关联对象： contract（合同对象）
     *          totalamount：合同总金额（货物金额+附件金额）
     *          extnum：附件数量
     */
    public void update(ExtCproduct newEp) {
        //1、计算更新后的附件金额
        double newMoney = 0d;
        if(newEp.getCnumber() != null && newEp.getPrice() != null) {
            newMoney =  newEp.getCnumber() * newEp.getPrice();
        }
        newEp.setAmount(newMoney);
        //2、查询更新前的附件金额
        ExtCproduct oldEp = extCproductDao.selectByPrimaryKey(newEp.getId());
        double oldMoney = oldEp.getAmount();
        //3、更新附件
        extCproductDao.updateByPrimaryKeySelective(newEp);
        //4、根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(newEp.getContractId());
        //5、设置购销合同的总金额（总金额-更新前的+更新后的）
        contract.setTotalAmount(contract.getTotalAmount() - oldMoney + newMoney);
        //6、更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }


    /**
     * 删除附件
     *  参数：id（附件id）
     */
    public void delete(String id) {
        //1、根据附件id查询附件
        ExtCproduct extc = extCproductDao.selectByPrimaryKey(id);
        //2、根据附件id删除数据
        extCproductDao.deleteByPrimaryKey(id);
        //3、根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extc.getContractId());
        //4、设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() - extc.getAmount());
        //5、设置合同的附件数
        contract.setExtNum(contract.getExtNum() - 1);
        //6、更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ExtCproduct> list = extCproductDao.selectByExample(example);
        return new PageInfo(list);
    }
}
