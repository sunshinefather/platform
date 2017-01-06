package com.platform.common.httpclient;

import java.util.Map;

/**
 * http客户端接口
 * @ClassName:  IHttpClient   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2016年11月24日 上午10:14:15
 */
public interface IHttpClient {
	
	public static final String CHARSET = "UTF-8";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    
    public static final int RESPONSE_OK = 200;
    
    public enum RequestMethod {
        GET, 
        POST,
        PUT,
        DELETE
    }

    //设置连接超时时间
    public static final int DEFAULT_CONNECTION_TIMEOUT = (5 * 1000);
    
    //设置读取超时时间
    public static final int DEFAULT_READ_TIMEOUT = (30 * 1000);
    
    public static final int DEFAULT_MAX_RETRY_TIMES = 3;

    public ResponseWrapper sendGet(String url)throws Exception;
    
    public ResponseWrapper sendDelete(String url) throws Exception;
    
    public ResponseWrapper sendPost(String url, String content)throws Exception;
    
    public ResponseWrapper sendPost(String url, Map<String, String> entity)throws Exception;
    
    public ResponseWrapper sendPut(String url, String content)throws Exception;
}