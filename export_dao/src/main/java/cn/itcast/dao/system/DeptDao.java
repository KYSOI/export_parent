package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeptDao {


    List<Dept> findAll(String companyId);

    //保存
    void save(Dept dept);

    //修改
    void update(Dept dept);

    //根据id查询部门
    Dept findById(String id);

    //删除
    void delete(String id);

    //根据父部门查询所有子部门的最大id编号
    String findMaxId(@Param("parentId") String parentId);
}
