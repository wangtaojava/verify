/**
 * llin 2019年4月11日下午12:13:01
 */
package cn.com.hf.verify.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author llin
 */
public class MD5EcryptUtil {
	
	public static boolean verifyMD5(String validateData, byte[] encryptData) throws Exception {
		String md5Result = md5(encryptData);
		return validateData.equalsIgnoreCase(md5Result);
	}

	public static String md5ToUpperCase(byte[] b) throws NoSuchAlgorithmException {
		return md5(b).toUpperCase();
	}

	public static String md5(byte[] b) throws NoSuchAlgorithmException {
		byte[] a = hashData("MD5", b);
		return ByteArrayToHexString(a);
	}

	private static byte[] hashData(String algorithm, byte[] b) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(b);
		byte[] digest = md.digest();
		return digest;
	}

	private static String ByteArrayToHexString(byte[] d) {
		if (d == null) {
			return "";
		} else if (d.length == 0) {
			return "";
		} else {
			int len = d.length * 2;
			byte[] strData = new byte[len];

			for (int i = 0; i < strData.length; ++i) {
				strData[i] = 48;
			}

			byte[] data = new byte[d.length + 1];
			data[0] = 0;
			System.arraycopy(d, 0, data, 1, d.length);
			BigInteger bi = new BigInteger(data);
			byte[] src = bi.toString(16).getBytes();
			int offset = strData.length - src.length;
			len = src.length;
			System.arraycopy(src, 0, strData, offset, len);
			return new String(strData);
		}
	}

}
