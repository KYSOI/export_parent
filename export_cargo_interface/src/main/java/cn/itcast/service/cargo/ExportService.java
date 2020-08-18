package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.ExportExample;
import cn.itcast.vo.ExportResult;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 * 报运单的service接口
 */
public interface ExportService {

    Export findById(String id);

    void save(Export export);

    void update(Export export);

    void delete(String id);

	PageInfo findAll(ExportExample example, int page, int size);

    void exportE(String id);
    //查询所有的报运单
    List<Export> findAll(ExportExample example);
    //更新保运结果
    void updateE(ExportResult result);
}
