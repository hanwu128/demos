/**
 * 
 */
package devicemanager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.lenovo.iot.devicemanager.util.ImgUtil;
import com.lenovo.iot.devicemanager.util.RandomUtil;

import junit.framework.TestCase;

/**
 * @desc devicemanager.ImageUtilTest
 * @author chench9@lenovo.com
 * @date 2017年11月8日
 */
public class ImageUtilTest extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateAuthCodeImage() {
		char[] charArr = RandomUtil.getRadomAuthCode(4);
		System.out.println(charArr);
		BufferedImage bufferedImage = ImgUtil.createAuthCodeImage(charArr);
		assertNotNull(bufferedImage);
		try {
			ImageIO.write(bufferedImage, "JPG", new File(System.getProperty("user.home"), "auth_code.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
