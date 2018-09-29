package test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.testng.Assert;


public class ShiroTest {

    @Test
    public void testHello() {
        try {
            //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
            Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
            //2、得到SecurityManager实例 并绑定给SecurityUtils
            SecurityManager securityManager = factory.getInstance();
            SecurityUtils.setSecurityManager(securityManager);
            //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
            //4、登录，即身份验证
            subject.login(token);
            Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
            //6、退出
            subject.logout();
        } catch (Exception e) {
            //5、身份验证失败
            e.printStackTrace();
        }
    }

    public void login(String configFile) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);
    }

    @Test
    public void testAllSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-all-success.ini");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2, principalCollection.asList().size());
    }

    @Test()
    public void testAllSuccessfulStrategyWithFail() {
        login("classpath:shiro-authenticator-all-fail.ini");
        Subject subject = SecurityUtils.getSubject();
    }
}
