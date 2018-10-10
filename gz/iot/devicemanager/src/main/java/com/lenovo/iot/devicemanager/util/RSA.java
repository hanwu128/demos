package com.lenovo.iot.devicemanager.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.apache.commons.codec.binary.Base64;

public class RSA {
	private static final int KEY_SIZE = 512;
	private static final int BLOCK_SIZE = 53;
	private static final int OUTPUT_BLOCK_SIZE = 64;
	private static String Algorithm = "RSA";

	public static String[] generateRSAKeyPair() {
		String[] keypair = new String[2];
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(Algorithm);
			keyPairGen.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGen.generateKeyPair();

			PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			String publicKeyString = getKeyString(publicKey);
			System.out.println("Public KEY===>" + publicKeyString);
			keypair[0] = publicKeyString;

			String privateKeyString = getKeyString(privateKey);
			System.out.println("Private KEY===>" + privateKeyString);
			keypair[1] = privateKeyString;

		} catch (Exception e) {
			System.err.println("Exception:" + e.getMessage());
		}
		
		return keypair;
	}

	public static PublicKey getPublicKey(String key) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new Base64().decode(key));
		KeyFactory keyFactory = KeyFactory.getInstance(Algorithm);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String key) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(new Base64().decode(key));
		KeyFactory keyFactory = KeyFactory.getInstance(Algorithm);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	private static String getKeyString(Key key) throws Exception {
		byte[] keyBytes = key.getEncoded();
		String s = new String(new Base64().encode(keyBytes));
		return s;
	}

	public static String encodeSecret(String publicKeyString, String content) throws Exception {
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");

			Key publicKey = getPublicKey(publicKeyString);
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey, new SecureRandom());
			byte[] data = content.getBytes("utf-8");
			int blocks = data.length / BLOCK_SIZE;
			int lastBlockSize = data.length % BLOCK_SIZE;
			byte[] encryptedData = new byte[(lastBlockSize == 0 ? blocks : blocks + 1) * OUTPUT_BLOCK_SIZE];
			for (int i = 0; i < blocks; i++) {
				rsaCipher.doFinal(data, i * BLOCK_SIZE, BLOCK_SIZE, encryptedData, i * OUTPUT_BLOCK_SIZE);
			}
			if (lastBlockSize != 0) {
				rsaCipher.doFinal(data, blocks * BLOCK_SIZE, lastBlockSize, encryptedData, blocks * OUTPUT_BLOCK_SIZE);
			}

			return new String(new Base64().encode(encryptedData));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw e;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new IOException("InvalidKey");
		} catch (ShortBufferException e) {
			e.printStackTrace();
			throw new IOException("ShortBuffer");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new IOException("UnsupportedEncoding");
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new IOException("IllegalBlockSize");
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new IOException("BadPadding");
		}
		
		return "";
	}

	public static String decodeSecret(String privateKeyString, String content) throws Exception {
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");

			byte[] decoded = null;
			decoded = new Base64().decode(content);
			Key privateKey = getPrivateKey(privateKeyString);

			rsaCipher.init(Cipher.DECRYPT_MODE, privateKey, new SecureRandom());
			int blocks = decoded.length / OUTPUT_BLOCK_SIZE;
			ByteArrayOutputStream decodedStream = new ByteArrayOutputStream(decoded.length);
			for (int i = 0; i < blocks; i++) {
				decodedStream.write(rsaCipher.doFinal(decoded, i * OUTPUT_BLOCK_SIZE, OUTPUT_BLOCK_SIZE));
			}

			return new String(decodedStream.toByteArray(), "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw e;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new IOException("InvalidKey");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new IOException("UnsupportedEncoding");
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new IOException("IllegalBlockSize");
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new IOException("BadPadding");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return "";
	}

	public static String signWithPrivateKey(String content, String privateKey, String input_charset) {
		try {
			PrivateKey priKey = getPrivateKey(privateKey);

			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));

			byte[] signed = signature.sign();

			return new String(new Base64().encode(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean verifyWithPublicKey(String content, String sign, String publicKey, String input_charset) {
		try {
			PublicKey pubKey = getPublicKey(publicKey);

			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initVerify(pubKey);
			signature.update(content.getBytes(input_charset));

			return signature.verify(new Base64().decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void genKeyPair() {
		try {
			//创建共私密钥对
			String[] keyPair = generateRSAKeyPair();
			String publicKeyString = keyPair[0];
			String privateKeyString = keyPair[1];

			// 私钥签名，公钥验签
			String testString = "Now I can successfully sign a file with a single algorithm (Either SHA1 OR SHA256), but I can't add the second signature. My only guess is that because I'm using the SAME certificate for both algorithms it doesn't like that. Do I need to have a different physical certificate for each algorithm? Just wondering because before the new year, I had been using a SHA256 certificate for years with a SHA1 algorithm and it validated fine on all Operating Systems.";
			// 签名
			String signString = signWithPrivateKey(testString, "87nmbWx1I1GdJlIwx63B6BgHMyuu0UfU", "utf-8");
			// 验签
			boolean result = verifyWithPublicKey(testString, signString, publicKeyString, "utf-8");
			System.out.println(result);

			// 公钥加密，私钥解密
			String cipherText = encodeSecret(publicKeyString, testString);
			// 加密
			System.out.println("cipher: " + cipherText);
			// 解密
			String plainText = decodeSecret(privateKeyString, cipherText);
			System.out.println("plain : " + plainText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		genKeyPair();
	}
}
