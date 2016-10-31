package com.platform.common.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static final String ALGORITHM = "AES";
	private static final String LOCAL_ENCODING = "UTF-8";
	
	/**
	 * 加密
	 * @Title: encrypt
	 * @Description: TODO  
	 * @param: @param content
	 * @param: @param authCode
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String encrypt(String content, String authCode) throws Exception {        
            KeyGenerator keyG = KeyGenerator.getInstance(ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG","SUN");
            secureRandom.setSeed(authCode.getBytes(LOCAL_ENCODING));  
            keyG.init(128,secureRandom);  
            SecretKey secretKey = keyG.generateKey();  
            byte[] enCodeFormat = secretKey.getEncoded();  
            SecretKeySpec key = new SecretKeySpec(enCodeFormat,ALGORITHM);  
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] byteContent = content.getBytes(LOCAL_ENCODING);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return byte2Hex(cipher.doFinal(byteContent));  
      }
	/**
	 * 解码
	 * @Title: decrypt
	 * @Description: TODO  
	 * @param: @param ciphertext
	 * @param: @param authCode
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String decrypt(String ciphertext, String authCode) throws Exception {
	            KeyGenerator keyG = KeyGenerator.getInstance(ALGORITHM);  
	            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG","SUN");  
	            secureRandom.setSeed(authCode.getBytes(LOCAL_ENCODING));  
	            keyG.init(128,secureRandom);
	            SecretKey secretKey = keyG.generateKey();  
	            byte[] enCodeFormat = secretKey.getEncoded();  
	            SecretKeySpec key = new SecretKeySpec(enCodeFormat,ALGORITHM);         
	            Cipher cipher = Cipher.getInstance(ALGORITHM);   
	            cipher.init(Cipher.DECRYPT_MODE, key); 
	            return new String(cipher.doFinal(hex2Byte(ciphertext)));
      }
	private  static String byte2Hex(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
      }
	private static byte[] hex2Byte(String hexStr) {  
	        if (hexStr.length() < 1){
	        	return null;
	        } 
	        byte[] result = new byte[hexStr.length()/2];
	        for (int i = 0;i< hexStr.length()/2; i++) { 
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1),16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2),16);  
	                result[i] = (byte)(high * 16 + low);
	        }
	        return result;  
      }
}
