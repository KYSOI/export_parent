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

    //@分页查询
    public PageInfo findAll(String loginCompanyId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Role> list = rodeDao.findAll(loginCompanyId);
        return new PageInfo(list);
    }

    //@保存
    public void save(Role role) {
        role.setId(UUID.randomUUID().toString());
        rodeDao.save(role);
    }

    //修改
    public void update(Role role) {
        rodeDao.update(role);
    }

    //@数据回显
    public Role findById(String id) {


        return rodeDao.findById(id);
    }

    //@删除
    public void delete(String id) {
        rodeDao.delete(id);
    }

    @Override
    public List<Role> findAll(String loginCompanyId) {
        return rodeDao.findAll(loginCompanyId);
    }

}
