/**
 * 
 */
package com.lenovo.iot.devicemanager.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.model.Account;
import com.lenovo.iot.devicemanager.model.AccountRole;
import com.lenovo.iot.devicemanager.model.JsonResp;
import com.lenovo.iot.devicemanager.service.AccountPermisionService;
import com.lenovo.iot.devicemanager.service.AccountService;
import com.lenovo.iot.devicemanager.util.IotMgrConstants;
import com.lenovo.iot.devicemanager.util.LoginedAccountHolder;
import com.lenovo.iot.devicemanager.util.Pagination;
import com.lenovo.iot.devicemanager.util.WebUtil;

import net.sf.json.JSONArray;

/**
 * @desc com.lenovo.iot.devicemanager.controller.AccountController
 * @author chench9@lenovo.com
 * @date 2017年11月6日
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountPermisionService accountPermisionService;
	
	/**
	 * 登录 <br />
	 * 登录成功时需要返回当前用户的权限和角色信息  <br />
	 * 同时,需要将登录用户信息保存在会话中.
	 * @param req
	 * @param resp
	 * @param loginName
	 * @param password
	 * @param authCode
	 * @return
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	@ResponseBody
	public Object login(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("loginName") String loginName,
			@RequestParam("password") String password,
			@RequestParam(value = "authCode", required = false) String authCode) {
		// 验证登录验证码是否正确
		if(authCode != null) {
			String cacheAuthCode = (String) req.getSession().getAttribute(IotMgrConstants.KEY_AUTH_CODE);
			if(cacheAuthCode == null || !cacheAuthCode.equalsIgnoreCase(authCode)) {
				logger.error("login auth code error, login name: {}, auth code: {}, cached auth code: {}", loginName, authCode, cacheAuthCode);
				return JsonResp.getJsonRespError(JsonResp.SC_AUTHCODE_INVALID, "auth code error");
			}
		}
		
		// 执行shiro认证
		Subject currentUser =  SecurityUtils.getSubject();
		//if(!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
			try {
				currentUser.login(token);
			} catch (UnknownAccountException e) {
				logger.error("account not found, login name: {}", loginName, e);
				return JsonResp.getJsonRespError(HttpServletResponse.SC_NOT_FOUND, "account not found");
			} catch(IncorrectCredentialsException e) {
				logger.error("account credentials incorrect, login name: {}, credentials: {} ", loginName, password, e);
				return JsonResp.getJsonRespError(JsonResp.SC_PASSWORD_INCORRECT, "account credentials incorrect");
			} catch (ConcurrentAccessException e) {
				logger.error("user has been authenticated: {}", loginName, e);
				return JsonResp.getJsonRespError(JsonResp.SC_AUTHENTICATED, "account authenticated already");
			} catch(DisabledAccountException e) {
				logger.error("accout has been disabled, login name: {}", loginName, e);
				return JsonResp.getJsonRespError(JsonResp.SC_DISABLED, "account has been disabled");
			} catch (AuthenticationException e) {
			    logger.error("account authenticate failure, login name: {}", loginName, e);
			    return JsonResp.getJsonRespError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "unknown error");
			}
		//}
		
		// 认证成功之后查询账户信息
		Account account = accountService.getAccountByLoginName(loginName);
		
		//认证成功之后返回用户权限列表
		List<String> permissionList = accountPermisionService.getAccountPermisionList(loginName);
		req.getSession().setAttribute(IotMgrConstants.KEY_LOGIN_USER, account);
		req.getSession().removeAttribute(IotMgrConstants.KEY_AUTH_CODE);
		
		JSONObject object = new JSONObject();
		object.put("account", WebUtil.formatAccount(account));
		object.put("permission", permissionList);
		
		return JsonResp.getJsonRespSuccess(object);
	}
	
	/**
	 * 退出登录
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/logout.do", method = RequestMethod.POST)
	@ResponseBody
	public Object logout(HttpServletRequest req, HttpServletResponse resp) {
		Account account = LoginedAccountHolder.getLoginedAccount();
		if(account != null) {
			logger.warn("account logout, login name: {}", account.getLoginName());
		}
		
		// 注意:退出登录时必须执行shiro退出,否则在浏览器端将报错: net::ERR_INCOMPLETE_CHUNKED_ENCODING
		Subject subject = SecurityUtils.getSubject();
		if(subject != null) {
			subject.logout();
		}
		
		req.getSession().invalidate();
		return JsonResp.getJsonRespSuccess();
	}
	
//	@RequestMapping(value = "/logout.do", method = RequestMethod.POST)
//	public void logout(HttpServletRequest req, HttpServletResponse resp) {
//		Account account = LoginedAccountHolder.getLoginedAccount();
//		if(account != null) {
//			logger.warn("account logout, login name: {}", account.getLoginName());
//		}
//		
//		try {
//			resp.getWriter().write(JSON.toJSONString(JsonResp.getJsonRespSuccess()));
//			resp.getWriter().flush();
//			resp.getWriter().close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			req.getSession().invalidate();
//		}
//	}
	
	/**
	 * 重置密码第一步：验证账号和验证码
	 * @param req
	 * @param resp
	 * @param loginName 账号登录名
	 * @param authCode 验证码
	 * @return
	 */
	@RequestMapping(value = "/resetpassword/step1.do", method = RequestMethod.POST)
	@ResponseBody
	public Object resetPasswordStep1(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("loginName") String loginName,
			@RequestParam("authCode") String authCode) {
		// 验证账户是否存在
		Account account = accountService.getAccountByLoginName(loginName);
		if(account == null) {
			logger.error("account not found! login name: {}", loginName);
			return JsonResp.getJsonRespError(HttpServletResponse.SC_NOT_FOUND, "account not found");
		}
		
		// 验证码错误
		//String cachedAuthCode = (String) req.getSession().getAttribute(IotMgrConstants.KEY_EMAIL_CODE);
		String cachedAuthCode = (String) req.getSession().getAttribute(loginName);
		if(cachedAuthCode == null || !cachedAuthCode.equalsIgnoreCase(authCode)) {
			logger.error("reset password step1 auth code error! login name: {}, auth code: {}, cached auth code: {}", loginName, authCode, cachedAuthCode);
			return JsonResp.getJsonRespError(JsonResp.SC_AUTHCODE_INVALID, "auth code error");
		}
		
		return JsonResp.getJsonRespSuccess(JsonResp.MSG_SUCCESS);
	}
	
	/**
	 * 重置密码第二步：重置账号密码
	 * @param req
	 * @param resp
	 * @param loginName 账户登录名
	 * @param authCode 验证码
	 * @param password 密码
	 * @return
	 */
	@RequestMapping(value = "/resetpassword/step2.do", method = RequestMethod.POST)
	@ResponseBody
	public Object resetPasswordStep2(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("loginName") String loginName,
			@RequestParam("authCode") String authCode,
			@RequestParam("password") String password) {
		// 密码不能为空
		if("".equals(password.trim())) {
			logger.error("account password can't be empty! login name: {}", loginName);
			return JsonResp.getJsonRespError(JsonResp.SC_PASSWORD_EMPTY, "password is empty");
		}
		
		// 验证账户是否存在
		Account account = accountService.getAccountByLoginName(loginName);
		if(account == null) {
			logger.error("account not found! login name: {}", loginName);
			return JsonResp.getJsonRespError(HttpServletResponse.SC_NOT_FOUND, "account not found");
		}
		
		// 验证码是否正确
		//String cachedAuthCode = (String) req.getSession().getAttribute(IotMgrConstants.KEY_EMAIL_CODE);
		String cachedAuthCode = (String) req.getSession().getAttribute(loginName);
		if(cachedAuthCode == null || !cachedAuthCode.equalsIgnoreCase(authCode)) {
			logger.error("reset password step2 auth code error! login name: {}, auth code: {}, cached auth code: {}", loginName, authCode, cachedAuthCode);
			return JsonResp.getJsonRespError(JsonResp.SC_AUTHCODE_INVALID, "auth code error");
		}
		
		//password = Md5.encryption(password);
		if(accountService.updateAccountPassword(password, account.getId()) > 0) {
			//req.getSession().removeAttribute(IotMgrConstants.KEY_EMAIL_CODE);
			req.getSession().removeAttribute(loginName);
			return JsonResp.getJsonRespSuccess();
		}
		
		logger.error("reset password error, login name: {}", loginName);
		//req.getSession().removeAttribute(IotMgrConstants.KEY_EMAIL_CODE);
		req.getSession().removeAttribute(loginName);
		return JsonResp.getJsonRespError();
	}
	
	/**
	 * 查看个人资料
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/meta.do", method = RequestMethod.GET)
	@ResponseBody
	public Object getAccountInformation(HttpServletRequest req, HttpServletResponse resp) {
		Account account = LoginedAccountHolder.getLoginedAccount();
		// 从数据库读取最新的账户信息
		account = accountService.getAccountByLoginName(account.getLoginName());
		return JsonResp.getJsonRespSuccess(WebUtil.formatAccount(account));
	}
	
	/**
	 * 保存个人资料
	 * @param req
	 * @param resp
	 * @param name 姓名
	 * @param phone 联系方式
	 * @return
	 */
	@RequestMapping(value = "/metasetting.do", method = RequestMethod.POST)
	@ResponseBody
	public Object setAccountInformation(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("name") String name,
			@RequestParam("phone") String phone) {
		Account account = LoginedAccountHolder.getLoginedAccount();
		if(accountService.updateAccountNamePhone(name, phone, account.getId()) > 0) {
			return JsonResp.getJsonRespSuccess();
		}
		
		logger.error("update account name and phone error, login name: {}", account.getLoginName());
		return JsonResp.getJsonRespError();
	}
	
	/**
	 * 账号管理-账号列表
	 * @param req
	 * @param resp
	 * @param start
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = "/list.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@RequiresPermissions({"account:manage"})
	public Object getAccountList(@RequestBody JSONObject page_object, HttpServletRequest req, HttpServletResponse resp) {
		Pagination page = new Pagination();
		if(page_object.containsKey("pagesize")) {
			page.setPagesize(page_object.getInteger("pagesize"));
		}
		if(page_object.containsKey("current")) {
			page.setCurrent(page_object.getInteger("current"));
		}
		if(page_object.containsKey("sort_orderby")) {
			page.setOrder_by(page_object.getString("sort_orderby"));
		}
		if(page_object.containsKey("sort_rule")) {
			page.setAsc(page_object.getString("sort_rule"));
		}
		
		List<Account> accountList = accountService.getAccountList(page);
		JSONArray objarray = new JSONArray();
		if (accountList != null) {
			for (Account acc : accountList) {
				JSONObject obj = new JSONObject();
				obj.put("id", acc.getId());
				obj.put("companyId", acc.getCompanyId());
				obj.put("name", acc.getName());
				obj.put("loginName", acc.getLoginName());
				obj.put("password", "");
				obj.put("enable", acc.getEnable());

				String email = acc.getEmail();
				if (email == null) {
					email = "";
				}
				obj.put("email", email);

				String phone = acc.getPhone();
				if (phone == null) {
					phone = "";
				}
				obj.put("phone", phone);

				String create_time = WebUtil.format(acc.getCreateTime().getTime());
				obj.put("create_time", create_time);
				String update_time = WebUtil.format(acc.getUpdateTime().getTime());
				obj.put("update_time", update_time);

				objarray.add(obj);
			}
		}

		JSONObject data = new JSONObject();
		data.put("total", accountList.size());
		data.put("list", objarray);
		data.put("pagesize", page.getPagesize());
		data.put("current", page.getCurrent());
		data.put("total", page.getTotal());

		return JsonResp.getJsonRespSuccess(data);
	}
	
	/**
	 * 添加二级账户
	 * @param req
	 * @param resp
	 * @param loginName
	 * @param password
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({"account:manage"})
	public Object addAccount(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("loginName") String loginName,
			@RequestParam("password") String password,
			@RequestParam("name") String name) {
		Account account = new Account();
		account.setLoginName(loginName);
		account.setName(name);
		account.setPassword(password);
		account.setCompanyId(LoginedAccountHolder.getLoginedAccount().getCompanyId());
		//if(WebUtil.verifyEmail(loginName)) {
			// 登录名为邮箱地址时设置为邮箱字段值
			account.setEmail(loginName);
		//}
		
		AccountRole accountRole = new AccountRole();
		accountRole.setLoginName(loginName);
		accountRole.setRoleName(IotMgrConstants.ROLE_ADMIN);
		
		try {
			if(accountService.addAccount(account, accountRole) > 0) {
				return JsonResp.getJsonRespSuccess();
			}
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			logger.error("account exits! login name: {}", loginName, e);
			return JsonResp.getJsonRespError(HttpServletResponse.SC_CONFLICT, "login name exists");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("add account error", e);
		}
		
		logger.error("add account error: {}", account);
		return JsonResp.getJsonRespError();
	}
	
	/**
	 * 账号管理 - 冻结/解冻账号
	 * @param req
	 * @param resp
	 * @param loginName
	 * @param enable
	 * @return
	 */
	@RequestMapping(value = "/enabled.do", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({"account:manage"})
	public Object enableAccount(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("loginName") String loginName,
			@RequestParam("enable") Integer enable) {
		Account account = accountService.getAccountByLoginName(loginName);
		if(account == null) {
			logger.error("account not found for enabled, login name: {}, enabled: {}", loginName, enable);
			return JsonResp.getJsonRespError(HttpServletResponse.SC_NOT_FOUND, "account not found");
		}
		
		int rows = 0;
		if(enable > 0) {
			rows = accountService.enabledAccount(loginName, 1);
		}else {
			rows = accountService.enabledAccount(loginName, 0);
		}
		
		if(rows > 0) {
			return JsonResp.getJsonRespSuccess();
		}
		
		logger.error("enabled account error, login name: {}, enable: {}", loginName, enable);
		return JsonResp.getJsonRespError();
	}
	
	
	/**
	 * 账号管理-查询账户权限列表
	 * @param req
	 * @param resp
	 * @param loginName
	 * @return
	 */
	@RequestMapping(value = "/permission.do", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions({"account:manage"})
	public Object getAccountPermission(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("loginName") String loginName) {
		List<String> permissionList = accountPermisionService.getAccountPermisionList(loginName);
		if(permissionList == null) {
			logger.error("query account permission list is empty, login name: {}", loginName);
			permissionList = new ArrayList<String>();
		}
		
		return JsonResp.getJsonRespSuccess(permissionList);
	}
	
	/**
	 *  账号管理 - 设置账号权限
	 * @param req
	 * @param resp
	 * @param loginName
	 * @param permission
	 * @return
	 */
	@RequestMapping(value = "/permissionsetting.do", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({"account:manage"})
	public Object setAccountPermission(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("loginName") String loginName,
			//@RequestParam("permission") String[] permission
			@RequestParam("permission") String permissionStr) {
		if(permissionStr == null || permissionStr.length() <= 0) {
			logger.error("update account permission is empty, login name: {}", loginName);
			return JsonResp.getJsonRespError(HttpServletResponse.SC_BAD_REQUEST, "permission is empty");
		}
		
		if(accountPermisionService.updateAccountPermission(loginName, Arrays.asList(permissionStr.split(","))) > 0) {
			return JsonResp.getJsonRespSuccess();
		}
		
		logger.error("update account permission error, login name: {}, permission: {}", loginName, permissionStr);
		return JsonResp.getJsonRespError();
	}
	
	/**
	 * 账号管理 - 删除账号
	 * @param req
	 * @param resp
	 * @param loginName
	 * @return
	 */
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({"account:manage"})
	public Object deleteAccount(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("loginName") String loginName) {
		if(accountService.deleteAccount(loginName) > 0) {
			return JsonResp.getJsonRespSuccess();
		}
		
		logger.error("delete account error, login name: {}", loginName);
		return JsonResp.getJsonRespError();
	}
	
}
