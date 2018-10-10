/**
 * 
 */
package com.lenovo.iot.devicemanager.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义shiro JDBC realm,重写读取角色权限列表实现
 * @desc com.lenovo.iot.devicemanager.service.impl.ShiroJdbcRealm
 * @author chench9@lenovo.com
 * @date 2017年11月6日
 */
public class ShiroJdbcRealm extends JdbcRealm {
	private static final Logger logger = LoggerFactory.getLogger(ShiroJdbcRealm.class);

	private final String TABLE_ACCOUNT = "account as a "; // 账户表
	private final String TABLE_ACCOUNT_ROLE = "account_role as ar "; // 账户角色表
	private final String TABLE_ACCOUNT_PERMISSION = "account_permission as ap "; // 账户权限表
	private final String TABLE_PERMISSION = "permission as p "; // 权限表
	
	private String _authenticationQuery = ""; // 认证用户信息
	private String _roleQuery = ""; // 查询角色列表
	private String _permissionsQuery = ""; // 查询权限列表
	
	{
		_authenticationQuery = new StringBuilder()
				.append("select password,enable from ").append(TABLE_ACCOUNT)
				.append("where login_name = ?")
				.toString();
		_roleQuery = new StringBuilder()
				.append("select role_name from ").append(TABLE_ACCOUNT_ROLE)
				.append("where login_name = ?")
				.toString();
		_permissionsQuery = new StringBuilder()
				.append("select p.val from ").append(TABLE_ACCOUNT_PERMISSION)
				.append("left join ").append(TABLE_PERMISSION)
				.append("on ap.perm_code = p.code ")
				.append("where ap.login_name = ?")
				.toString();
	}
	
	public ShiroJdbcRealm() {
		setAuthenticationQuery(_authenticationQuery);
		setPermissionsQuery(_permissionsQuery);
		setUserRolesQuery(_roleQuery);
	}

	/**
	 * 用户认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        Connection conn = null;
        SimpleAuthenticationInfo info = null;
        try {
            conn = dataSource.getConnection();
            String[] result = getPasswordEnabledForUser(conn, username);
            String password = result[0];
            if (password == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            }
            int enabled = Integer.valueOf(result[1]);
            if(enabled <= 0) {
            	throw new DisabledAccountException("account been disabled for user [" + username + "]");
            }
            
            info = new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
        } catch (SQLException e) {
            final String message = "There was a SQL error while authenticating user [" + username + "]";
            if (logger.isErrorEnabled()) {
            	logger.error(message, e);
            }

            // Rethrow any SQL errors as an authentication exception
            throw new AuthenticationException(message, e);
        } finally {
            JdbcUtils.closeConnection(conn);
        }

        return info;
	}
	
	/**
	 * 查询用户密码和激活状态
	 * @param conn
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	private String[] getPasswordEnabledForUser(Connection conn, String username) throws SQLException {
        String[] result = new String[2];
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(authenticationQuery);
            ps.setString(1, username);

            // Execute query
            rs = ps.executeQuery();

            // Loop over results - although we are only expecting one result, since usernames should be unique
            boolean foundResult = false;
            while (rs.next()) {
                // Check to ensure only one row is processed
                if (foundResult) {
                    throw new AuthenticationException("More than one user row found for user [" + username + "]. Usernames must be unique.");
                }
                result[0] = rs.getString(1);
                result[1] = rs.getString(2);
                foundResult = true;
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }

        return result;
    }

	/**
	 * 查询用户权限列表
	 */
	@Override
	protected Set<String> getPermissions(Connection conn, String username, Collection<String> roleNames)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
        Set<String> permissions = new LinkedHashSet<String>();
        try {
            ps = conn.prepareStatement(permissionsQuery);
            ps.setString(1, username);
            try {
                // Execute query
                rs = ps.executeQuery();
                // Loop over results and add each returned role to a set
                while (rs.next()) {
                    String permissionString = rs.getString(1);
                    // Add the permission to the set of permissions
                    permissions.add(permissionString);
                }
            } finally {
                JdbcUtils.closeResultSet(rs);
            }
        } finally {
            JdbcUtils.closeStatement(ps);
            if(rs != null){
            	JdbcUtils.closeResultSet(rs);
            }
        }
        return permissions;
	}

	/**
	 * 查询用户角色列表
	 */
	@Override
	protected Set<String> getRoleNamesForUser(Connection conn, String username) throws SQLException {
		PreparedStatement ps = null;
        ResultSet rs = null;
        Set<String> roleNames = new LinkedHashSet<String>();
        try {
            ps = conn.prepareStatement(userRolesQuery);
            ps.setString(1, username);

            // Execute query
            rs = ps.executeQuery();

            // Loop over results and add each returned role to a set
            while (rs.next()) {

                String roleName = rs.getString(1);

                // Add the role to the list of names if it isn't null
                if (roleName != null) {
                    roleNames.add(roleName);
                } else {
                    if (logger.isWarnEnabled()) {
                    	logger.warn("Null role name found while retrieving role names for user [{}]", username);
                    }
                }
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
        return roleNames;
	}
	
}
