/**
 * 
 */
package com.lenovo.iot.devicemanager.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 图片工具
 * @desc com.lenovo.iot.devicemanager.util.ImgUtil
 * @author chench9@lenovo.com
 * @date 2017年11月8日
 */
public class ImgUtil {
	
	/**
	 * 绘制验证码图片
	 * @param charArr 验证码字符数组
	 * @return
	 */
	public static BufferedImage createAuthCodeImage(char[] charArr) {
		int width = 68;
		int height = 30;
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = bufferedImage.getGraphics();
		Color color = new Color(250, 150, 255);
		g.setColor(color);
		g.fillRect(0, 0, width, height);
		
		Random random = new Random();
		int size = charArr.length;
		for(int i = 0; i < size; i++) {
			// 设置验证码字符随机颜色
			g.setColor(new Color(random.nextInt(88), random.nextInt(188), random.nextInt(255)));
			// 画出随机字符码
			g.drawString(String.valueOf(charArr[i]), (i * 16) + 5, 20);
		}
		return bufferedImage;
	}
	
	
	private ImgUtil() {
	}
	
}
