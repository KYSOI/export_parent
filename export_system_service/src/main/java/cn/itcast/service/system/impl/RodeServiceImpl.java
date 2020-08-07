package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;

import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RodeServiceImpl implements RoleService {

    @Autowired
    private RoleDao rodeDao;

    //根据id查询(数据回显)
    public Role findById(String id) {
        return rodeDao.findById(id);
    }

    //根据id角色,查询此角色已经具有的模块
    public List<String> findModulesByRoleId(String roleId) {
        return rodeDao.findModulesByRoleId(roleId);
    }

    //分页查询
    public PageInfo findAll(String loginCompanyId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Role> list = rodeDao.findAll(loginCompanyId);
        return new PageInfo(list);
    }

    //查询所有
    public List<Role> findAll(String loginCompanyId) {
        return rodeDao.findAll(loginCompanyId);
    }

    //保存
    public void save(Role role) {
        role.setId(UUID.randomUUID().toString());
        rodeDao.save(role);
    }

    //修改
    public void update(Role role) {
        rodeDao.update(role);
    }

    //删除
    public void delete(String id) {
        rodeDao.delete(id);
    }

    //实现对角色分配权限
    public void updateRoleModule(String roleid, String moduleIds) {
        //调用dao根据角色id删除角色模块中间表数据
        rodeDao.deleteRoleModule(roleid);
        //将moduleIds差分成模块id数组
        String[] mids = moduleIds.split(",");
        for (String moduleId : mids) {
        //    调用dao项角色模块中间表存储数据
            rodeDao.saveRoleModule(roleid,moduleId);
        }
    }

}
