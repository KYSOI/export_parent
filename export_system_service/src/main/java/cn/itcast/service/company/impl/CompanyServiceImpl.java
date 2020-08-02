package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;

    public List<Company> findAll() {
        return companyDao.findAll();
    }

    //保存企业
    public void save(Company company) {
        //由于id是string类型,需要手动设置(唯一的字符串标志uuid)
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

    //修改企业
    public Company findById(String id) {
      return companyDao.findById(id);

    }

   //更新
    public void update(Company company) {
        companyDao.update(company);
    }

   //删除企业
    public void delete(String id) {
        companyDao.delete(id);
    }
}
