package cn.itcast.dao.user;

import cn.itcast.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


public interface UserDao {

    List<User> findAll(String companyId);

    void update(User user);

    void save(User user);

    User findUserById(String id);

    void delete(String id);
}
