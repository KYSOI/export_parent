package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.service.cargo.ContractProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;


    public void save(ContractProduct contractProduct) {

    }


    public void update(ContractProduct contractProduct) {

    }


    public void delete(String id) {

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
}
