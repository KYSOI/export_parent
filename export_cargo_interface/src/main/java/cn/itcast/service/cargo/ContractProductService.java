package cn.itcast.service.cargo;


import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 购销合同货物service接口
 */
public interface ContractProductService {

	/**
	 * 保存
	 */
	void save(ContractProduct contractProduct);

	/**
	 * 更新
	 */
	void update(ContractProduct contractProduct);

	/**
	 * 删除
	 */
	void delete(String id);

	/**
	 * 根据id查询
	 */
	ContractProduct findById(String id);

	/**
	 * 分页查询
	 */
	PageInfo findAll(ContractProductExample example, int page, int size);

	/**
	 * 批量保存货物
	 */
	void saveAll(List<ContractProduct> list);


}
