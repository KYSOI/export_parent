package cn.itcast.service.cargo;


import cn.itcast.domain.cargo.ExportProduct;
import cn.itcast.domain.cargo.ExportProductExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 报运单商品service
 */
public interface ExportProductService {

	List<ExportProduct> findAll(ExportProductExample exportProductExample);
}
