package until;


import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置自定义认证处理的过滤器，并加入到FilterChain的过程。
 * 将自定义的Filter加入到security中的FilterChain中去
 * 其实和默认的UsernamePasswordAuthenticationFilter并没有什么区别，
 * 但是这里主要是学会将自定义的Filter加入到security中的FilterChain中去，实际上这个方法中，
 * 一般会直接验证用户输入的和通过用户名从数据库里面查到的用户的密码是否一致，如果不一致，就抛异常，否则继续向下执行。
 * @auther zhangsq on 2017-9-6.
 */

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String USERNAME = "j_username";
    private static final String PASSWORK = "j_password";

    /**
     * 用户登录验证方法入口
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if(!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Authentication method not supported:"+request.getMethod());
        }
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);
        if(null == username){
            username = "";
        }
        if(null == password){
            password = "";
        }
        username = username.trim();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);
        setDetails(request,authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * 获取密码
     * @param request
     * @return
     */
    protected String obtainPassword(HttpServletRequest request){
        Object obj = request.getParameter(PASSWORK);
        return null == obj ? "" : obj.toString();
    }

    /**
     * 获取用户名
     * @param request
     * @return
     */
    protected String obtainUsername(HttpServletRequest request){
        Object object = request.getParameter(USERNAME);
        return null == object ? "" : object.toString().trim().toLowerCase();
    }

}
