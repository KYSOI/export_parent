package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.vo.ContractProductVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    //分页
    public PageInfo findAll(int page,int size,ContractExample example) {
        PageHelper.startPage(page,size);
        List<Contract> list = contractDao.selectByExample(example);
        return new PageInfo(list);
    }

    //保存
    public void save(Contract contract) {
        contract.setId(UUID.randomUUID().toString());
        contract.setTotalAmount(0.0); //初始化合同总金额 0
        contract.setState(0); //草稿状态
        contract.setCreateTime(new Date()); //创建时间
        contractDao.insertSelective(contract);
    }

    //更新
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    //根据id删除
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    //根据id查询
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    //根据船期查询出货表数据
    public List<ContractProductVo> findByShipTime(String shipTime) {
        return contractDao.findByShipTime(shipTime);
    }
}
