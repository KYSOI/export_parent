package cn.itcast.web.shiro;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

/**
 自定义realm域
 *1继承谷类AuthorizingRealm
 * 2实现父类中的两个抽象方法(认证,授权)
 */
public class AuthRealm  extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    /**
     *授权方法
     *   本质:提供操作用户的权限数据(权限名称集合)
     *   参数:principlCollection(安全名称集合),只能获取唯一的安全数据(User用户对象)
     *   返回值
     *          AuthorizationInfo:授权数据(权限名称集合)
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        List<Module> modules = moduleService.findByUser(user);
        //提取所有的模块名称,set集合(自动去重)
        HashSet<String> permissions = new HashSet<>();
        for (Module module : modules) {
            permissions.add(module.getName());
        }
        //构造返回值
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 认证方法
     *      参数：AuthenticationToken（UsernamePasswordToken）
     *          用户登录时封装的用户名（邮箱）和密码
     *      返回值：
     *          AuthenticationInfo ：认证数据
     *              1、安全数据（用户对象）
     *              2、密码（非必须）：为了方便操作数据库密码
     *              3、realm域名称：当前类名
     *      业务:
     *          1、获取登录输入的用户名和密码
     *          2、调用service查询
     *          3、判断用户是否存在
     *          3.1 用户存在，构造info返回值
     *          3.2 用户不存在，返回null，自动抛出异常
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String email = upToken.getUsername();
        String password = String.valueOf(upToken.getPassword());
        //掉用service查询
        User user = userService.findByEmail(email);
        //判断用户是否存在
        if (user != null) {
            //    存在,构造info返回值
            return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        } else {
            //用户不存在返回null,自动抛出异常
            return null;
        }
    }
}
