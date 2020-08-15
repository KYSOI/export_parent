package cn.itcast.service.cargo;

import cn.itcast.vo.ContractProductVo;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface ContractService {


    //分页查询

    /**
     * 查询条件：companyId
     */
    PageInfo findAll(int page, int size, ContractExample example);

    //保存
    void save(Contract contract);

    //修改
    void update(Contract contract);

    //删除
    void delete(String id);

    //根据id查询
    Contract findById(String id);


    List<ContractProductVo> findByShipTime(String shipTime);
}
