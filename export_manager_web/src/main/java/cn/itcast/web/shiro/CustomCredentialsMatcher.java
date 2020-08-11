package cn.itcast.web.shiro;

import cn.itcast.util.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 自定义密码比较器
 *1继承父类SimpleCredentialsMatcher
 * 重写密码比较方法
 */

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获取用户登录输入的邮箱密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String email = upToken.getUsername();
        String password = String.valueOf(upToken.getPassword());
        //获取数据库密码
        String dbpassword = (String) info.getCredentials();
        //对用户输入的密码进行加密
        password = Encrypt.md5(password, email);
        //比较两个密码
        return dbpassword.equals(password);
    }
}
