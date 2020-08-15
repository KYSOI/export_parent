package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.util.DeptCodeGens;
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

    /**
     * 保存
     * 调用部门id的工具类
     * 参数一：父部门id
     * 参数二：当前子部门中的最大id
     */
    public void save(Dept dept) {
        //1.获取当前保存的部门的父部门id（一级部门，父部门id为null）
        String parentId = dept.getParent().getId();
        //2.根据父部门查询所有子部门的最大id编号
        String maxId = deptDao.findMaxId(parentId);
        //3.调用工具类计算下个部门的id编号
        String deptId = DeptCodeGens.getSubCode(parentId, maxId);
        //4.设置部门id
        dept.setId(deptId);
        //5.调用dao保存
        deptDao.save(dept);
    }

    //更新
    public void update(Dept dept) {
        deptDao.update(dept);

    }

    //根据id查部门
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    //删除部门
    public void delete(String id) {
        deptDao.delete(id);
    }

}
