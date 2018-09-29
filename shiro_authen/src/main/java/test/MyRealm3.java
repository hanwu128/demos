package test;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

public class MyRealm3 implements Realm {

    //返回一个唯一的Realm名字
    @Override
    public String getName() {
        return "li";
    }

    //判断此Realm是否支持此Token
    @Override
    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken类型的Token
        return token instanceof UsernamePasswordToken;
    }

    //根据Token获取认证信息
    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        String username = (String) token.getPrincipal();
        //获取密码
        String password = new String((char[]) token.getCredentials());
        if (!"zhang".equals(username)) {
            //用户名不存在
            throw new UnknownAccountException("用户名不存在!");
        }
        if (!"123".equals(password)) {
            //密码错误
            throw new IncorrectCredentialsException("密码错误!");
        }
        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
