package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;

import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleDao moduleDao;

    //@Ove分页查询rride
    public PageInfo findAll(int page, int size) {
        PageHelper.startPage(page, size);
        List<Module> list = moduleDao.findAll();
        return new PageInfo(list);
    }

    //@Override查询回显下拉
    public List<Module> findAll() {
        return moduleDao.findAll();

    }

    //@Override保存
    public void save(Module module) {
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    //@Override修改
    public void update(Module module) {
        moduleDao.update(module);
    }

    //@Override根据id查询
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    //@删除
    public void delete(String id) {
        moduleDao.delete(id);
    }
}
