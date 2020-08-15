package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;//附件dao

    @Autowired
    private ContractDao contractDao;


    /**
     * 保存附件
     * extCproduct ： 附件对象
     * 属性：
     * price：单价
     * cnumber：数量
     * amount：附件总金额
     * contractId ： 合同id
     * contractProductId ：货物id
     */
    public void save(ExtCproduct ext) {
        //    计算保存附件的总金额
        double newMoney = 0.0;
        if (ext.getPrice() != null && ext.getCnumber() != null) {
            newMoney = ext.getPrice() * ext.getCnumber();
        }
        ext.setAmount(newMoney);
        //    设置附件的id
        ext.setId(UUID.randomUUID().toString());
        //    保存附近
        extCproductDao.insertSelective(ext);
        //    根据合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(ext.getContractId());
        //    设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() + newMoney);
        //    设置合同附件数
        contract.setExtNum(contract.getExtNum() + 1);
        //    更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 进入修改页面
     */

    public void update(ExtCproduct newExt) {
        // 设置修改后的总金额
        double newMoney = 0.0;
        if (newExt.getPrice() != null && newExt.getCnumber() != null) {
            newMoney = newExt.getPrice() * newExt.getCnumber();
        }
        newExt.setAmount(newMoney);
        //    查询修改之前的总金额
        ExtCproduct oldExt = extCproductDao.selectByPrimaryKey(newExt.getId());
        Double oldMoney = oldExt.getAmount();//修改之前的金额
        //   更新附件
        extCproductDao.updateByPrimaryKeySelective(newExt);
        //    根据合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(newExt.getContractId());
        //    设置合同总金额
        contract.setTotalAmount(contract.getTotalAmount() - oldMoney + newMoney);
        //    更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除
     * id:附件id
     */
    public void delete(String id) {
        //    根据附件id查询附件对象
        ExtCproduct ext = extCproductDao.selectByPrimaryKey(id);
        //    删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //    根据合同id查询
        Contract contract = contractDao.selectByPrimaryKey(ext.getContractId());
    //    修改合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() - ext.getAmount());
    //    修改附件数
        contract.setExtNum(contract.getExtNum() - 1);
    //    更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    //根据id查询
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    //分页

    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        PageHelper.startPage(page, size);

        List<ExtCproduct> list = extCproductDao.selectByExample(example);
        return new PageInfo(list);
    }
}
