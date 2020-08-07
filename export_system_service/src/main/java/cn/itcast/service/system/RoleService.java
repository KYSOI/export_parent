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
    //根据id角色,查询此角色已经具有的模块
    List<String> findModulesByRoleId(String roleId);
    //实现对角色分配权限
    void updateRoleModule(String roleid, String moduleIds);
}
