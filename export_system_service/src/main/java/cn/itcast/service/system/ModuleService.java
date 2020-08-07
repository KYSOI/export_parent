package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ModuleService {
    PageInfo findAll(int page, int size);
    //查询回显给模块
    List<Module> findAll();
    //保存
    void save(Module module);
    //修改
    void update(Module module);
    //查询根据id
    Module findById(String id);

    void delete(String id);
    //根据用户查询所有权限
    List<Module> findByUser(User user);
}
