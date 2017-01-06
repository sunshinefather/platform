package com.platform.common.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.platform.common.utils.StringUtils;

/**
 * http本地客户端实现类
 * @ClassName:  NativeHttpClient   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2016年11月24日 上午10:13:54
 */
public class NativeHttpClient implements IHttpClient {
	
    private final int _connectionTimeout;
	private final int _readTimeout;
    private final int _maxRetryTimes;
    
    private Map<String,String> header = new HashMap<String,String>();
    
    private String default_charset = CHARSET;
    
    public NativeHttpClient(){
         this(0,0,0);
    }
    
    public NativeHttpClient(int connectionTimeout,int readTimeout,int maxRetryTimes,Map<String,String> headers,String charset){
    	this(connectionTimeout,readTimeout,maxRetryTimes);
    	setHeader(headers);
    	setDefault_charset(charset);
    }
    
    public NativeHttpClient(Map<String,String> headers,String charset){
    	this(0,0,0);
    	setHeader(headers);
    	setDefault_charset(charset);
    }
    
    public NativeHttpClient(int connectionTimeout,int readTimeout,int maxRetryTimes){
    	if(connectionTimeout!=0){
    		this._connectionTimeout=connectionTimeout;
    	}else{
    		this._connectionTimeout=DEFAULT_CONNECTION_TIMEOUT;
    	}
    	
    	if(readTimeout!=0){
    		this._readTimeout =readTimeout;
    	}else{
    		this._readTimeout=DEFAULT_READ_TIMEOUT;
    	}
    	
    	if(maxRetryTimes!=0){
    		this._maxRetryTimes=maxRetryTimes;
    	}else{
    		this._maxRetryTimes=DEFAULT_MAX_RETRY_TIMES;
    	}
    }
	@Override
	public ResponseWrapper sendGet(String url) throws Exception {
		return  doRequest(url, null, RequestMethod.GET);
	}

	@Override
	public ResponseWrapper sendDelete(String url) throws Exception {
		return doRequest(url, null, RequestMethod.DELETE);
	}
	
	@Override
	public ResponseWrapper sendPost(String url, String content)
			throws Exception {
		return doRequest(url, content, RequestMethod.POST);
	}
	
	@Override
	public ResponseWrapper sendPost(String url,Map<String,String> entity) throws Exception {
		StringBuilder content =new StringBuilder();
		if(!entity.isEmpty()){
			int size = entity.size(),i=0;
			for(Map.Entry<String,String> entry: entity.entrySet()){
				i++;
				content.append(entry.getKey());
				content.append("=");
				content.append(entry.getValue());
				if(i<size){
					content.append("&");
				}
			}
		}
		return doRequest(url, content.toString(), RequestMethod.POST);
	}

	@Override
	public ResponseWrapper sendPut(String url, String content) throws Exception {
		return doRequest(url, content, RequestMethod.PUT);
	}
	 
	
	public ResponseWrapper doRequest(String url, String content, 
	            RequestMethod method) throws Exception {
	        ResponseWrapper response = null;
	        for (int retryTimes = 0;;retryTimes++) {
	            try {
	                response = _doRequest(url, content, method);
	                break;
	            } catch (SocketTimeoutException e) {
	            	if (retryTimes >= _maxRetryTimes) {
                        throw new Exception("连接失败:"+e.getMessage());
                    }
	          }
	        }
	        return response;
	    }
	 
	 private ResponseWrapper _doRequest(String url, String content, 
	            RequestMethod method) throws Exception{
		    System.setProperty("sun.net.http.allowRestrictedHeaders","true");
			HttpURLConnection conn = null;
			OutputStream out = null;
			StringBuffer sb = new StringBuffer();
			ResponseWrapper wrapper = new ResponseWrapper();
			
			try {
				URL aUrl = new URL(url);
				conn = (HttpURLConnection) aUrl.openConnection();
				conn.setRequestMethod(method.name());
				conn.setConnectTimeout(_connectionTimeout);
				conn.setReadTimeout(_readTimeout);
				conn.setUseCaches(false);
				if(header!=null && !header.isEmpty()){
					for(Map.Entry<String,String> entry: header.entrySet()){
						conn.addRequestProperty(entry.getKey(),entry.getValue());
					}
				}
				conn.addRequestProperty("Connection", "Keep-Alive");
				conn.addRequestProperty("Accept-Charset",default_charset);
				conn.addRequestProperty("Charset", default_charset);
				if(RequestMethod.POST == method){
					conn.addRequestProperty("Content-Type",CONTENT_TYPE_FORM);
				}else{
					conn.addRequestProperty("Content-Type","*/*");
				}
				//conn.setRequestProperty("User-Agent","Safari");//部分服务器必须需要该参数(Safari,MSIE 10.0,Chrome,Firefox)
				if(StringUtils.isEmpty(content)) {
					conn.setDoOutput(false);
				} else {
					conn.setDoOutput(true);
					byte[] data = content.getBytes(default_charset);
					conn.setRequestProperty("Content-Length", String.valueOf(data.length));
					out = conn.getOutputStream();
					out.write(data);
					out.flush();
				}
	            
	            int status = conn.getResponseCode();
	            InputStream in = null;
	            if (status / 100 == 2) {
	                in = conn.getInputStream();
	            } else {
	                in = conn.getErrorStream();
	            }
	            
	            if (null != in) {
		            InputStreamReader reader = new InputStreamReader(in, default_charset);
		            char[] buff = new char[1024];
		            int len;
		            while ((len = reader.read(buff)) > 0) {
		                sb.append(buff, 0, len);
		            }
	            }
	            
	            Map<String,List<String>>  map = conn.getHeaderFields();
	            String responseContent = sb.toString();
	            wrapper.responseCode = status;
	            wrapper.responseContent = responseContent;
	            wrapper.responseHeaders=map;
	            if (status >= 200 && status < 300) {
	            	
				}else{
				    throw new Exception("请求失败:"+status);
				}
	            
			} catch (SocketTimeoutException e) {
			    throw new Exception("连接超时:"+e.getMessage());
	            
	        } catch (IOException e) {
	            throw new Exception("IO异常"+e.getMessage());
	            
			} finally {
				if (null != out) {
					out.close();
				}
				if (null != conn) {
					conn.disconnect();
				}
			}
			return wrapper;
		}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public Map<String, String> getHeader() {
		return header;
	}
	
	public void setDefault_charset(String default_charset) {
		this.default_charset = default_charset;
	}
}