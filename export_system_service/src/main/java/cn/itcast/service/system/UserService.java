package cn.itcast.service.system;



import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {

    PageInfo<User> findAll(String companyId, int page, int size);

    //保存用户
    void save(User user);

    User findUserById(String id);

    void update(User user);

    void delete(String id);

    User findById(String id);

    List<String> findRolesByUserId(String id);

    void changeRole(String userid, String[] roleIds);
    //根据用户邮箱查询用户
    User findByEmail(String email);
}
