package cn.itcast.service.cargo;



import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;

import java.util.List;
import java.util.Map;

/**
 * 生产厂家的service接口
		 */
public interface FactoryService {

	//查询所有
	public List<Factory> findAll(FactoryExample example);
}