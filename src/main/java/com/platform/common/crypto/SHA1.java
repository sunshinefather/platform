package com.platform.common.crypto;

import java.security.MessageDigest;

public class SHA1 {
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
        final StringBuilder sha1Code = new StringBuilder();
        try{
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        md.update(message.getBytes());  
        byte[] bits = md.digest(); 
        for(int i=0;i<bits.length;i++){  
        	String shaHex = Integer.toHexString(bits[i] & 0xFF);
			if (shaHex.length() < 2) {
				sha1Code.append(0);
			}
			sha1Code.append(shaHex);
        }  
        }catch(Exception e){
        	return "";
        }	
        return sha1Code.toString();
	}
}
