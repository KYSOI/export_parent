package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService  {
    PageInfo findAll(String loginCompanyId, int page, int size);
    //保存
    void save(Role role);
    //修改
    void update(Role role);

    //数据回显
    Role findById(String id);

    void delete(String id);

    List<Role> findAll(String loginCompanyId);
}
