package com.platform.common.persistence.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;
/**
 * 标识MyBatis的DAO
 * @ClassName:  MyBatisDao   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月5日 下午5:41:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface MyBatisDao {
	
	String value() default "";

}