package com.platform.common.utils;

import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
/**
 * 生成唯一性ID
 * @ClassName:  IdGen   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月6日 上午11:45:06
 */
@Service
@Lazy(false)
public class IdGen implements SessionIdGenerator {
	
	public static String uuid() {
		return StringUtils.getUUIDCode();
	}

	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}
}