package com.platform.common.httpclient;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * http返回值包装类
 * @ClassName:  ResponseWrapper   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2016年11月24日 上午11:14:26
 */
public class ResponseWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private static final int RESPONSE_CODE_NONE = -1;
    
    public int responseCode = RESPONSE_CODE_NONE;
    
    public String responseContent;
    
    public Map<String,List<String>>  responseHeaders;
    
    public boolean isOkResponse() {
        if (responseCode / 100 == 2){
        	return true;
        }
        return false;
    }
    
	@Override
	public String toString() {
		return responseContent;
	}
}