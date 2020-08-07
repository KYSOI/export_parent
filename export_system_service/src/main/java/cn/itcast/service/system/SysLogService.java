package cn.itcast.service.system;

import cn.itcast.domain.system.SysLog;
import com.github.pagehelper.PageInfo;

public interface SysLogService {
    //分页查询
    PageInfo findAll(String companyId, int page, int size);

    void save(SysLog log);
}
