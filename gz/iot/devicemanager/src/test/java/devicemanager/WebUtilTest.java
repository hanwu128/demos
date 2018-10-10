/**
 * 
 */
package devicemanager;

import com.lenovo.iot.devicemanager.util.WebUtil;

import junit.framework.TestCase;

/**
 * @desc devicemanager.WebUtilTest
 * @author chench9@lenovo.com
 * @date 2017年11月10日
 */
public class WebUtilTest extends TestCase {

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
	
	public void testVerifyEmail() {
		String email = "mad_web@163.com";
		boolean result = WebUtil.verifyEmail(email);
		assertEquals(result, true);
		
	}

}
