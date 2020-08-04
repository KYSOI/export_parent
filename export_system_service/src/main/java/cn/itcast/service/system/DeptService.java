package cn.itcast.service.system;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DeptService {
    //部门分页查询
    PageInfo findAll(String companyId,int page,int size);

    //    查询所有部门
    List<Dept> findAll(String companyId);
    //保存
    void save(Dept dept);
    //更新
    void update(Dept dept);

    Dept findById(String id);

    void delete(String id);
}
