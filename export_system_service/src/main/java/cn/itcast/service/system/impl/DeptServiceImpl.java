package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao deptDao;

    //分页查询企业
    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Dept> list = deptDao.findAll(companyId);
        return new PageInfo(list);
    }

    //查询所有部门
    public List<Dept> findAll(String companyId) {

        return deptDao.findAll(companyId);
    }

    //保存
    public void save(Dept dept) {
        dept.setId(UUID.randomUUID().toString());
        deptDao.save(dept);
    }

    //更新
    public void update(Dept dept) {
        deptDao.update(dept);

    }

    //@Override
    //根据id查部门
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    //删除部门
    public void delete(String id) {
        deptDao.delete(id);
    }


}
