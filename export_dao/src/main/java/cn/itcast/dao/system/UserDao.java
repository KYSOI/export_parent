package cn.itcast.dao.system;

import cn.itcast.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserDao {

    List<User> findAll(String companyId);

    void update(User user);

    void save(User user);

    User findUserById(String id);

    void delete(String id);

    List<String> findRolesByUserId(String id);

    void changeRole(@Param("userId") String userId, @Param("roleId") String roleId);

    void deleteUserRole(String userid);

}
