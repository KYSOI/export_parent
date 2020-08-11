package cn.itcast.service.company;

import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CompanyService {

    List<Company> findAll();

    //保存
    void save(Company company);

    //根据id查询
    Company findById(String id);

    //更新
    void update(Company company);

    // 根据id删除
    void delete(String id);

    // 通过PageByHelper分页
    PageInfo findPageByHelper(int page, int size);
}
