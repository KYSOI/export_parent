package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //根据id角色,查询此角色已经具有的模块
    List<String> findModulesByRoleId(String roleId);

    //查询全部用户
    List<Role> findAll(String companyId);

    //根据id删除
    int delete(String id);

    //根据角色id删除角色模块中间表数据
    void deleteRoleModule(String roleid);

    //添加
    int save(Role role);

    //更新
    int update(Role role);


    //实现对角色分配权限
    void saveRoleModule(@Param("roleId") String roleId, @Param("moduleId") String moduleId);

}