/**
 * 
 */
package com.lenovo.iot.devicemanager.util;

import java.util.Random;

/**
 * 随机数工具
 * @desc com.lenovo.iot.devicemanager.util.RandomUtil
 * @author chench9@lenovo.com
 * @date 2017年11月8日
 */
public class RandomUtil {
	/**
	 * 生成随机验证码
	 * @param size 验证码位数
	 * @return
	 */
	public static char[] getRadomAuthCode(int size) {
		// 验证码字符集合
		char[] randomArr = new char[size];
		char[] sourceArr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		int len = sourceArr.length;
		Random random = new Random();
		for(int i = 0; i < size; i++) {
			randomArr[i] = sourceArr[random.nextInt(len)];
		}
		return randomArr;
	}
	
	private RandomUtil() {
	}

}
