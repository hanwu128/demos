package com.lenovo.iot.devicemanager.util;
 
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DesUtil {
 
    private final static String DES = "DES";
	public static final String PRIVATE_KEY = "lenovo&*&*#moc*&*&";
 
	// public static void main(String[] args) throws Exception {
	// String data = "100001,zhangsan,zhangsan,1029934821374824";
	// String key = "lenovo&*&*#moc*&*&";
	// System.err.println(encrypt(data, key));
	// System.err.println(decrypt(encrypt(data, key), key));
	// }
    private DesUtil(){}
    /**
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        byte[] strs = new Base64().encode(bt);
        return new String(strs);
    }
 
    /**
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        Base64 decoder = new Base64();
        byte[] buf = decoder.decode(data);
        byte[] bt = decrypt(buf,key.getBytes());
        return new String(bt);
    }
 
    /**
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
     
     
    /**
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
 
/*
    public static String decrypt_lience(String realPath, String plainText)
    {
      try
      {
        String privatekey = FileUtil.readFileByLines(realPath + "/rsaPrivateKey.txt");
        String strPub = FileUtil.readFileByLines(realPath + "/desEncrypt.txt");

        byte[] sikey = new BASE64Decoder().decodeBuffer(privatekey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(sikey);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Map keyMap = new HashMap();
        keyMap.put("private", privateKey);
        
        Key key = (Key)keyMap.get("private");

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, key);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(strPub);
        byte[] b = cipher.doFinal(b1);
        
        String desSource = new String(b);

        String source = new DES(desSource).decrypt(plainText);
        return source; 
      } catch (Exception e) {
    	  
      }
      return null;
    }
*/
}