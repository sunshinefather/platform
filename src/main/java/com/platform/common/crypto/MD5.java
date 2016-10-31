package com.platform.common.crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	/**
	 * 加密
	 * @Title: encrypt
	 * @Description: TODO  
	 * @param: @param message
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
    public static String encrypt(String message) {
        final StringBuffer md5Code = new StringBuffer();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(message.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5Code.append(String.valueOf(0));
                }
                md5Code.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return md5Code.toString().toUpperCase();
    }
}
