package orj.worf.core.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES工具类
 * 优点：加密、解密速度快
 * 缺点：因为是对称加密，加密解密双方都必须知道秘钥。
 * 缺点解决方案：可以由客户端生成一个随机秘钥，然后用RSA公钥加密AES秘钥，服务端通过RSA私钥解密得到AES秘钥，之后请求通过AES加密传输
 * @author LiuZhenghua
 * 2017年9月18日 下午3:40:02
 */
public class AESUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(AESUtil.class);
	private static Map<String, SecretKeySpec> secretKeys = new HashMap<String, SecretKeySpec>();
	
	/**
	 * encrypt a string using AES algorithm
	 * @param source The string that need to be encrypted
	 * @param secretKey The secret key of AES.
	 * @return The encrypted result in hex string.  
	 * @author LiuZhenghua
	 * 2017年9月21日 上午10:43:10
	 */
	public static String encrypt(String source, String secretKey) {
		String encryptedResult = null;
		try {
			SecretKeySpec keySpec = secretKeys.get(secretKey);
			if (keySpec == null) {
				keySpec = initSecretKeySpec(secretKey);
			}
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			System.out.println("source bytes:" + StringUtils.join(source.getBytes(), ','));
			byte[] resultByteArr = cipher.doFinal(source.getBytes());
			System.out.println("secreted bytes:" + StringUtils.join(resultByteArr, ','));
			encryptedResult = toHexString(resultByteArr);
		} catch (Exception e) {
			logger.error("Encrypting string by AES algorithm got an exception, source string:{}", source, e);
		}
		return encryptedResult;
	}
	
	/**
	 * Decrypt a string using AES algorithm
	 * @param hexEncryptedStr The string that encrypted in hex
	 * @param secretKey The secret key of AES.
	 * @return The decrypted result in hex string.
	 * @author LiuZhenghua
	 * 2017年9月21日 上午10:59:05
	 */
	public static String decrypt(String hexEncryptedStr, String secretKey) {
		String sourceStr = null;
		try {
			SecretKeySpec keySpec = secretKeys.get(secretKey);
			if (keySpec == null) {
				keySpec = initSecretKeySpec(secretKey);
			}
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			System.out.println(StringUtils.join(toByteArray(hexEncryptedStr), ','));
			byte[] resultByteArr = cipher.doFinal(toByteArray(hexEncryptedStr));
			System.out.println(StringUtils.join(resultByteArr, ','));
			sourceStr = new String(resultByteArr);
		} catch (Exception e) {
			logger.error("Decrypting string by AES algorithm got an exception, decrypted string:{}", hexEncryptedStr, e);
		}
		return sourceStr;
	}
	
	private static synchronized SecretKeySpec initSecretKeySpec(String secretKey) throws NoSuchAlgorithmException {
		SecretKeySpec keySpec = secretKeys.get(secretKey);
		if (keySpec != null) {
			return keySpec;
		}
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(secretKey.getBytes());
		kgen.init(128, secureRandom);
		byte[] encodedFormat = kgen.generateKey().getEncoded();
		System.out.println(toHexString(encodedFormat));
		keySpec = new SecretKeySpec(encodedFormat, "AES");
		secretKeys.put(secretKey, keySpec);
		return keySpec;
	}
	
	/**
	 * change byte array into hex string. 
	 * @author LiuZhenghua
	 * 2017年9月19日 下午1:55:52
	 */
	private static String toHexString(byte[] buf) {
		StringBuffer buffer = new StringBuffer(buf.length * 2);
		for (byte b : buf) {
			String hexString = Integer.toHexString(b & 0xff);
			if (hexString.length() == 1) {
				buffer.append('0');
			}
			buffer.append(hexString.toUpperCase());
		}
		return buffer.toString();
	}
	
	/**
	 * change hex string into byte array.
	 * @author LiuZhenghua
	 * 2017年9月19日 下午1:56:20
	 */
	private static byte[] toByteArray(String hexString) {
		if (hexString.length() % 2 != 0) {
			throw new RuntimeException("The length of hex string must be an odd number.");
		}
		byte[] result = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			 result[i/2] = (byte)Integer.parseInt(hexString.substring(i, i + 2), 16);
		}
		return result;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			String secretKey = "xfdasgfar2342!34";
			String encrypt = encrypt("3DFD-6A38-C699-3DD0", secretKey);
			System.out.println("加密后的内容：" + encrypt);
			
			encrypt = "3EE3A2B8DE61648C8DD94B8797E6B139F6DF2897D4EC26AD5B0A7B28FA70DCF4";
			String decrypt = decrypt(encrypt, secretKey);
			System.out.println("解密后的内容：" + decrypt);
		}
	}
}
