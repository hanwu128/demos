/**
 * 
 */
package com.lenovo.iot.devicemanager.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.model.Account;
import com.lenovo.iot.devicemanager.model.JsonResp;
import com.lenovo.iot.devicemanager.service.AccountService;
import com.lenovo.iot.devicemanager.util.EmailUtil;
import com.lenovo.iot.devicemanager.util.ImgUtil;
import com.lenovo.iot.devicemanager.util.IotMgrConstants;
import com.lenovo.iot.devicemanager.util.RandomUtil;

/**
 * 验证码
 * @desc com.lenovo.iot.devicemanager.controller.AuthCodeController
 * @author chench9@lenovo.com
 * @date 2017年11月8日
 */
@Controller
@RequestMapping("/authcode")
public class AuthCodeController {
	private static final Logger logger = LoggerFactory.getLogger(AuthCodeController.class);
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 动态生成验证码
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	@RequestMapping(value = "/image.url", method = RequestMethod.GET)
	public void image(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		char[] charArr = RandomUtil.getRadomAuthCode(4);
		if(logger.isDebugEnabled()) {
			logger.debug("auth code: {}", charArr);
		}
		
		req.getSession().setAttribute(IotMgrConstants.KEY_AUTH_CODE, String.valueOf(charArr));
		BufferedImage bufferedImage = ImgUtil.createAuthCodeImage(charArr);
		ImageIO.write(bufferedImage, "JPG", resp.getOutputStream());
	}
	
	/**
	 * 对验证码进行base64编码
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/image.code", method = RequestMethod.GET)
	@ResponseBody
	public Object imageBase64(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		char[] charArr = RandomUtil.getRadomAuthCode(4);
		if(logger.isDebugEnabled()) {
			logger.debug("auth code: {}", charArr);
		}
		
		req.getSession().setAttribute(IotMgrConstants.KEY_AUTH_CODE, String.valueOf(charArr));
		BufferedImage bufferedImage = ImgUtil.createAuthCodeImage(charArr);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "JPG", bos);
		return DatatypeConverter.printBase64Binary(bos.toByteArray());
		//return Base64.encodeBase64String(bos.toByteArray());
	}
	
	/**
	 * 获取邮箱验证码
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/code.do", method = RequestMethod.POST)
	@ResponseBody
	public Object emailCode(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody JSONObject json) {
		String loginName = json.getString("loginName");
		//Account account = LoginedAccountHolder.getLoginedAccount();
		Account account = accountService.getAccountByLoginName(loginName);
		if(account == null) {
			logger.error("account not found, login name: {}", loginName);
			return JsonResp.getJsonRespError(HttpServletResponse.SC_NOT_FOUND, "account not found");
		}
		
		// 避免反复调用接口
		//Object code = req.getSession().getAttribute(IotMgrConstants.KEY_EMAIL_CODE);
		Object code = req.getSession().getAttribute(loginName);
		if(code != null) {
			logger.error("verification code has been send email, login name: {}", account.getLoginName());
			return JsonResp.getJsonRespSuccess();
		}
		code = new String(RandomUtil.getRadomAuthCode(4));
		
		String fromNickName = "IoT";
		String subject = "验证码";
		String text = "验证码：" + code;
		String[] receiverArr = new String[] {account.getEmail()};
		if(EmailUtil.getInstance().sendTextEmail(fromNickName, Arrays.asList(receiverArr), null , subject, text)) {
			//req.getSession().setAttribute(IotMgrConstants.KEY_EMAIL_CODE, code);
			req.getSession().setAttribute(loginName, code);
			return JsonResp.getJsonRespSuccess();
		}
		
		logger.error("send email verification code error");
		return JsonResp.getJsonRespError();
	}
	
}
