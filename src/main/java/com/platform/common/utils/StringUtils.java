package com.platform.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.google.common.collect.Lists;
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";
    
    public static byte[] getBytes(String str){
    	if (str != null){
    		try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
    	}else{
    		return null;
    	}
    }
    
    public static String toString(byte[] bytes){
    	try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
    }
    
    public static boolean inString(String str, String... strs){
    	if (str != null){
        	for (String s : strs){
        		if (str.equals(trim(s))){
        			return true;
        		}
        	}
    	}
    	return false;
    }

	public static String replaceHtml(String html) {
		if (isBlank(html)){
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}
	
	public static String replaceMobileHtml(String html){
		if (html == null){
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}
	
	public static String toHtml(String txt){
		if (txt == null){
			return "";
		}
		return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}

	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("UTF-8").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String abbr2(String param, int length) {
		if (param == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false;
		boolean isHTML = false;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHTML = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHTML) {
				isHTML = false;
			}
			try {
				if (!isCode && !isHTML) {
					n += String.valueOf(temp).getBytes("UTF-8").length;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (n <= length - 3) {
				result.append(temp);
			} else {
				result.append("...");
				break;
			}
		}
		// 取出字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)",
				"$1$2");
		// 去掉不需要结束标记的HTML标记
		temp_result = temp_result
				.replaceAll(
						"</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
						"");
		// 去掉成对的HTML标记
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>",
				"$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = Lists.newArrayList();
		while (m.find()) {
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}
	
	public static Double toDouble(Object val){
		if (val == null){
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}

	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}

	public static Integer toInteger(Object val){
		return toLong(val).intValue();
	}
	
	public static String getMessage(String code, Object[] args) {
		LocaleResolver localLocaleResolver = (LocaleResolver) SpringContextHolder.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
	}
	
	public static String getRemoteAddr(HttpServletRequest request){
		String ipAddress = request.getHeader("x-forwarded-for");  
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getHeader("Proxy-Client-IP");  
        }  
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getRemoteAddr();  
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                InetAddress inet=null;  
                try {  
                    inet = InetAddress.getLocalHost();  
                } catch (UnknownHostException e) {  
                    e.printStackTrace();  
                }  
                ipAddress= inet.getHostAddress();  
            }  
        }  
        if(ipAddress!=null && ipAddress.length()>15){
            if(ipAddress.indexOf(",")>0){  
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
            }  
        }  
        return ipAddress;   
	}

	/**
	 * 驼峰命名1
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld" 
	 */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
	 * 驼峰命名2
	 * @return
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    /**
	 * 驼峰命名3
	 * @return
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
 
    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     * 例如：row.user.id
     * 返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString){
    	StringBuilder result = new StringBuilder();
    	StringBuilder val = new StringBuilder();
    	String[] vals = split(objectString, ".");
    	for (int i=0; i<vals.length; i++){
    		val.append("." + vals[i]);
    		result.append("!"+(val.substring(1))+"?'':");
    	}
    	result.append(val.substring(1));
    	return result.toString();
    }

	public  static String toUTF8(String str){
		if (isEmpty(str)) {
			return "";
		}
		String retStr = str;
		try{
		byte b[];
		b = str.getBytes("ISO8859_1");

		for (int i = 0; i < b.length; i++) {
			byte b1 = b[i];
			if (b1 == 63)
				break;
			else if (b1 > 0)
				continue;
			else if (b1 < 0) {
				retStr = new String(b, "utf8");
				break;
			}
		}
		}catch(Exception e){
		}
		return retStr;
	}
	
	public static String getUUIDCode() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String getRandomCode(int type, int length, String excludeChars) {
		if (length <= 0) {
			return "";
		}
		StringBuffer code = new StringBuffer();
		int i = 0;
		Random r = new Random();

		switch (type) {
		case 0://纯数字
			while (i < length) {
				int t = r.nextInt(10);
				if ((excludeChars == null) || (excludeChars.indexOf(String.valueOf(t)) < 0)) {
					code.append(t);
					i++;
				}
			}
			break;
		case 1://大写字母+小写字母
			while (i < length) {
				int t = r.nextInt(123);
				if (((t < 97) && ((t < 65) || (t > 90)))
						|| ((excludeChars != null) && (excludeChars.indexOf((char) t) >= 0)))
					continue;
				code.append((char) t);
				i++;
			}

			break;
		case 2://数字+大写字母+小写字母
			while (i < length) {
				int t = r.nextInt(123);
				if (((t < 97) && ((t < 65) || (t > 90)) && ((t < 48) || (t > 57)))
						|| ((excludeChars != null) && (excludeChars.indexOf((char) t) >= 0)))
					continue;
				code.append((char) t);
				i++;
			}

			break;
		case 3://数字+大写字母
			while (i < length) {
				int t = r.nextInt(91);
				if (((t < 65) && ((t < 48) || (t > 57)))
						|| ((excludeChars != null) && (excludeChars.indexOf((char) t) >= 0)))
					continue;
				code.append((char) t);
				i++;
			}

			break;
		case 4://数字+小写字母
			while (i < length) {
				int t = r.nextInt(123);
				if (((t < 97) && ((t < 48) || (t > 57)))
						|| ((excludeChars != null) && (excludeChars.indexOf((char) t) >= 0)))
					continue;
				code.append((char) t);
				i++;
			}
			break;
		case 5://纯大写
			while (i < length) {
				int t = r.nextInt(91);
				if ((t < 65)
						|| ((excludeChars != null) && (excludeChars.indexOf((char) t) >= 0)))
					continue;
				code.append((char) t);
				i++;
			}

			break;
		case 6://纯小写
			while (i < length) {
				int t = r.nextInt(123);
				if ((t < 97)
						|| ((excludeChars != null) && (excludeChars.indexOf((char) t) >= 0)))
					continue;
				code.append((char) t);
				i++;
			}

		}

		return code.toString();
	}
	/**
	 * 清除字符串前的零
	 */
	public static String clearZeroBefore(String str){
		if(str == null || "".equals(str))
			return str;
		int len = calcZeroBefore(str);
        if(len>0){
        	return str.replaceAll("^(0{"+len+"})", "");
        }else{
        	return str;
        }
        
	}
	
	/**
	 * 字符串前面补零
	 */
	public static String fillZeroBefore(String str,int len){
		if(str == null || "".equals(str))
			return str;
		if(str.length() < len){
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < len - str.length(); i++) {
				buffer.append("0");
			}
			str = buffer.append(str).toString();
		}
		return str;
	}
	
	/**
	 * 计算字符串前面有几个0
	 */
	public static int calcZeroBefore(String str){
		if(str == null || "".equals(str))
			return 0;
		char[] arr = str.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if((int)arr[i] != 48){
				return i;
			}
		}
		return 0;
	}
	/**
	 * 抽取数字字母汉字
	 * @Title: extractNLCC
	 * @Description: TODO  
	 * @param: @param content
	 * @param: @return      
	 * @return: String
	 * @author: sunshine  
	 * @throws
	 */
	public static String extractNLCC(String content){
		Pattern p = Pattern.compile("[a-zA-Z0-9\u4e00-\u9fa5]");
		Matcher m = p.matcher(content);
		StringBuffer sb=new StringBuffer();
		List<String> result=new ArrayList<String>();
		while(m.find()){
			result.add(m.group());
		}
		for(String s1:result){
			sb.append(s1);
		}
		return sb.toString();
	}
	/**
	 * 判断是否是数字或小数
	 * @Title: isBigDecimal
	 * @Description: TODO  
	 * @param: @param str
	 * @param: @return      
	 * @return: boolean
	 * @author: sunshine  
	 * @throws
	 */
	public static boolean isBigDecimal(String str){
		if(isNotEmpty(str)){
			return str.matches("^[-]?([0-9]+([.]([0-9]+))?)$");
		}else{
			return false;
		}
	}
}