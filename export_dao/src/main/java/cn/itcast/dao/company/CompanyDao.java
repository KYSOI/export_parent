package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;

import java.util.List;

public interface CompanyDao {
    List<Company> findAll();

    void save(Company company);

    Company findById(String id);

    void update(Company company);

    void delete(String id);
}
