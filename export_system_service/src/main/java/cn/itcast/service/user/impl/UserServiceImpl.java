package cn.itcast.service.user.impl;

import cn.itcast.dao.user.UserDao;
import cn.itcast.domain.user.User;
import cn.itcast.service.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    //根据企业id分页查询用户
    public PageInfo<User> findAll(String companyId, int page, int size) {
        //设置分页
        PageHelper.startPage(page, size);
        //盗用dao
        List<User> list = userDao.findAll(companyId);
        return new PageInfo<>(list);
    }



    //保存
    public void save(User user) {
        user.setId(UUID.randomUUID().toString());
        userDao.save(user);
    }

    @Override
    public User findUserById(String id) {
        return userDao.findUserById(id);
    }

    //修改
    public void update(User user) {
        userDao.update(user);
    }

    //删除
    public void delete(String id) {
        userDao.delete(id);
    }
}
