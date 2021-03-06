package cn.itcast.dao.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;

import java.util.List;

/**
 *
 */
public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    void delete(String moduleId);

    //添加用户
    void save(Module module);

    //更新用户
    void update(Module module);

    //查询全部
    List<Module> findAll();


    //根据belong查询模块
    List<Module> findByBelong(int belong);
    // 根据userid查询多个表获取用户的菜单权限
    List<Module> findByUserId(String id);
}