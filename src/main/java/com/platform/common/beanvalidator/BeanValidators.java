package com.platform.common.beanvalidator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * JSR303 Hibernate Validator工具类
 * @ClassName:  BeanValidators   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月20日 下午5:02:40
 */
public class BeanValidators {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void validateWithException(Validator validator, Object object, Class<?>... groups)
			throws ConstraintViolationException {
		Set constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	public static List<String> extractMessage(ConstraintViolationException e) {
		return extractMessage(e.getConstraintViolations());
	}

    /**
     * 转换ConstraintViolationException中的Set<ConstraintViolations>中为List<message>
     * @Title: extractMessage
     * @Description: TODO  
     * @param: @param e
     * @param: @return      
     * @return: List<String>
     * @author: sunshine  
     * @throws
     */
	@SuppressWarnings("rawtypes")
	public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
		List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getMessage());
		}
		return errorMessages;
	}

	public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e) {
		return extractPropertyAndMessage(e.getConstraintViolations());
	}

    /**
     * 转换Set<ConstraintViolation>为Map<property, message>
     * @Title: extractPropertyAndMessage
     * @Description: TODO  
     * @param: @param constraintViolations
     * @param: @return      
     * @return: Map<String,String>
     * @author: sunshine  
     * @throws
     */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations) {
		Map<String, String> errorMessages = Maps.newHashMap();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return errorMessages;
	}

	public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
	}

	@SuppressWarnings("rawtypes")
	public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations) {
		return extractPropertyAndMessageAsList(constraintViolations, " ");
	}

	public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
	}

    /**
     * 转换Set<ConstraintViolations>为List<message>
     * @Title: extractPropertyAndMessageAsList
     * @Description: TODO  
     * @param: @param constraintViolations
     * @param: @param separator
     * @param: @return      
     * @return: List<String>
     * @author: sunshine  
     * @throws
     */
	@SuppressWarnings("rawtypes")
	public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations,
			String separator) {
		List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
		}
		return errorMessages;
	}
}