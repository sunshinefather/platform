package com.platform.common.utils;

import java.io.Serializable;
import java.security.SecureRandom;
import org.activiti.engine.impl.cfg.IdGenerator;
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
public class IdGen implements IdGenerator, SessionIdGenerator {

	private static SecureRandom random = new SecureRandom();
	
	public static String uuid() {
		return StringUtils.getUUIDCode();
	}
	
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Encodes.encodeBase62(randomBytes);
	}
	
	@Override
	public String getNextId() {
		return IdGen.uuid();
	}

	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}
}