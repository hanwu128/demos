package devicemanager;

import com.lenovo.iot.devicemanager.util.Md5;

import junit.framework.TestCase;

public class Md5UtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testMD5() {
		String password = "111111";
		String encode =	Md5.encryption(password);
		assertEquals("96e79218965eb72c92a549dd5a330112", encode);
	}

}
