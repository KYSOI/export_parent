package cn.itcast.service.user;

import cn.itcast.domain.user.User;
import com.github.pagehelper.PageInfo;

public interface UserService {

    PageInfo<User> findAll(String companyId, int page, int size);

    //保存用户
    void save(User user);

    User findUserById(String id);

    void update(User user);

    void delete(String id);
}
