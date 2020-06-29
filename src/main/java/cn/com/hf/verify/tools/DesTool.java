package cn.com.hf.verify.tools;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class DesTool {

	private static volatile DesTool instance;
	
	public static DesTool getInstance(){
		if(instance == null){
			synchronized (DesTool.class) {
				if(instance == null){
					instance = new DesTool();
				}
			}
		}
		return instance;
	}
	
	private DesTool() {
	}

	/**
	 * 根据参数生成KEY
	 */
//	private void setKey(String strKey) {
//		try {
//			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
//			 DESKeySpec keySpec = new DESKeySpec(strKey.getBytes());  
//			 keyFactory.generateSecret(keySpec);  
//			 this.key = keyFactory.generateSecret(keySpec); 
//			
//			 keyFactory = null;
//			/*KeyGenerator _generator = KeyGenerator.getInstance("DES");
//			_generator.init(new SecureRandom(strKey.getBytes()));
//			this.key = _generator.generateKey();
//			_generator = null;*/
//		} catch (Exception e) {
//			throw new RuntimeException(
//					"[setKey]Error initializing SqlMap class. Cause: " + e);
//		}
//	}
	private Key getKey(String strKey) {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
			DESKeySpec keySpec = new DESKeySpec(strKey.getBytes());  
			keyFactory.generateSecret(keySpec);  
			return keyFactory.generateSecret(keySpec); 
			
//			keyFactory = null;
			/*KeyGenerator _generator = KeyGenerator.getInstance("DES");
			_generator.init(new SecureRandom(strKey.getBytes()));
			this.key = _generator.generateKey();
			_generator = null;*/
		} catch (Exception e) {
			throw new RuntimeException(
					"[setKey]Error initializing SqlMap class. Cause: " + e);
		}
	}

	/**
	 * 加密String明文输入,String密文输出
	 */
	public String getEncString(String strMing,String strKey) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		// BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = strMing.getBytes("UTF-8");
			byteMi = this.getEncCode(byteMing,strKey);
			strMi = new String(Base64.encode(byteMi));
		} catch (Exception e) {
			throw new RuntimeException(
					"[getEncString]Error initializing SqlMap class. Cause: " + e);
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMi;
		/*byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = strMing.getBytes("UTF8");
			byteMi = this.getEncCode(byteMing);
			strMi = base64en.encode(byteMi);
		} catch (Exception e) {
			throw new RuntimeException(
					"[getEncString]Error initializing SqlMap class. Cause: " + e);
		} finally {
			base64en = null;
			byteMing = null;
			byteMi = null;
		}
		return strMi;*/
	}

	/**
	 * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 * @return
	 */
	public String getDesString(String strMi,String strKey) {
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			byteMi = Base64.decode(strMi);
			byteMing = this.getDesCode(byteMi,strKey);
			strMing = new String(byteMing, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(
					"[getDesString]Error initializing SqlMap class. Cause: " + e);
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMing;
/*		BASE64Decoder base64De = new BASE64Decoder();
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			byteMi = base64De.decodeBuffer(strMi);
			byteMing = this.getDesCode(byteMi);
			strMing = new String(byteMing, "UTF8");
		} catch (Exception e) {
			throw new RuntimeException(
					"[getDesString]Error initializing SqlMap class. Cause: " + e);
		} finally {
			base64De = null;
			byteMing = null;
			byteMi = null;
		}
		return strMing;*/
	}

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	private byte[] getEncCode(byte[] byteS,String strKey) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, getKey(strKey));
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			throw new RuntimeException(
					"[getEncCode]Error initializing SqlMap class. Cause: " + e);
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	private byte[] getDesCode(byte[] byteD,String strKey) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, getKey(strKey));
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			throw new RuntimeException(
					"[getDesCode]Error initializing SqlMap class. Cause: " + e);
		} finally {
			cipher = null;
		}
		return byteFina;
	}
	
	public static String hexStringToString(String s) {
	    if (s == null || s.equals("")) {
	        return null;
	    }
	    s = s.replace(" ", "");
	    byte[] baKeyword = new byte[s.length() / 2];
	    for (int i = 0; i < baKeyword.length; i++) {
	        try {
	            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    try {
	        s = new String(baKeyword, "UTF-8");
	        new String();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	    return s;
	}
	

//	public static void main(String[] args) {
//		String miStr = "KCOlux9K2mdYebRcJ/VEJMM8inVatVRwdwHi2fExYEf+ssbVb6A5Y85QmRL6TpnG6u3AUQW8WeTGfoHlKSiPGiDoHH7Xx7y4ssVbvjK6zB/FdhyRVyiwsu7ir/MD/uDbqmzRXkHZinNCbCaDw8Ptkw==";
//		String key = "555555555555555";
//		String desString = DesTool.getInstance().getDesString(miStr, key);
//		System.out.println(desString);
//		
//		String str16 = "e4baa4e69893e68890e58a9f";
//		
//		System.out.println(hexStringToString(str16));
//	}
	
}
