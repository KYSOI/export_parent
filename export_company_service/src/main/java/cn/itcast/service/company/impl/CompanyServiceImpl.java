package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;


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

    /**
     *通过PageHepler分页
     * 1设置分页参数
     * 2调用dao查询全部
     * 3构造返回值
     * */
    @Override
    public PageInfo findPageByHelper(int page, int size) {
        //设置分页参数
        PageHelper.startPage(page, size);//当前也,每页条数
        List<Company> list = companyDao.findAll();
        return new PageInfo(list);//有残构造
    }
}
