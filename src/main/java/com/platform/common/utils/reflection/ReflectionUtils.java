package com.platform.common.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.platform.common.utils.StringUtils;
/**
 * 反射工具类
 * 
 * @ClassName: Reflections
 * @Description:TODO
 * @author: sunshine
 * @date: 2015年5月15日 下午5:54:25
 */
public class ReflectionUtils {
	
	private static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);
    
	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	/**
	 * 调用Getter方法获取对象值,方法名不用加get前缀 支持多级，如：对象名.对象名.方法
	 * 
	 * @Description: TODO
	 * @Title: invokeGetter
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param propertyName
	 * @param: @return
	 * @return: Object
	 * @throws
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		Object object = obj;
		for (String name : StringUtils.split(propertyName, ".")) {
			String getterMethodName = GETTER_PREFIX
					+ StringUtils.capitalize(name);
			if(object!=null){
				object = invokeMethod(object, getterMethodName, new Class[] {},
						new Object[] {});
			}else{
				break;
			}
		}
		return object;
	}

	/**
	 * 调用Setter方法设置对象值,方法名不用加set前缀, 仅匹配方法名,。 支持多级，如：对象名.对象名.方法
	 * 
	 * @Description: TODO
	 * @Title: invokeSetter
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param propertyName
	 * @param: @param value
	 * @return: void
	 * @throws
	 */
	public static void invokeSetter(Object obj, String propertyName,
			Object value) {
		Object object = obj;
		String[] names = StringUtils.split(propertyName, ".");
		for (int i = 0; i < names.length; i++) {
			if (i < names.length - 1) {
				String getterMethodName = GETTER_PREFIX
						+ StringUtils.capitalize(names[i]);
				object = invokeMethod(object, getterMethodName, new Class[] {},
						new Object[] {});
			} else {
				String setterMethodName = SETTER_PREFIX
						+ StringUtils.capitalize(names[i]);
				invokeMethodByName(object, setterMethodName,
						new Object[] { value });
			}
		}
	}

	/**
	 * 直接读取对象属性值,不经过getter函数
	 * 
	 * @Description: TODO
	 * @Title: getFieldValue
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param fieldName
	 * @param: @return
	 * @return: Object
	 * @throws
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {

		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("属性名[" + fieldName + "] 在 ["
					+ obj + "]中不存在 ");
		}
		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			
		}
		return result;
	}

	/**
	 * 直接设置对象属性值,不经过setter函数
	 * 
	 * @Description: TODO
	 * @Title: setFieldValue
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param fieldName
	 * @param: @param value
	 * @return: void
	 * @throws
	 */
	public static void setFieldValue(final Object obj, final String fieldName,
			final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("属性名[" + fieldName + "] 在 ["
					+ obj + "]中不存在 ");
		}
		try {
			if (value != null) {
				String returntype = field.getType().toString();
				if (returntype.contains("java.lang.Double")) {
					field.set(obj, new Double(value.toString()));
				} else if (returntype.contains("java.lang.Integer")) {
					field.set(obj, new Integer(value.toString()));
				} else if (returntype.contains("java.lang.Float")) {
					field.set(obj, new Float(value.toString()));
				} else if (returntype.contains("java.math.BigDecimal")) {
					field.set(obj, new BigDecimal(value.toString()));
				} else if (returntype.contains("java.lang.Boolean")) {
					field.set(obj, new Boolean(value.toString()));
				} else if (returntype.contains("java.lang.Short")) {
					field.set(obj, new Short(value.toString()));
				} else if (returntype.contains("java.lang.Byte")) {
					field.set(obj, new Byte(value.toString()));
				} else if (returntype.contains("java.lang.Long")) {
					field.set(obj, new Long(value.toString()));
				} else {
					field.set(obj, value);
				}
			} else {
				field.set(obj, null);
			}
		} catch (IllegalAccessException e) {
		}
	}

	/**
	 * 调用对象方法,匹配函数名+参数
	 * 
	 * @Description: TODO
	 * @Title: invokeMethodByName
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param methodName
	 * @param: @param args
	 * @param: @return
	 * @return: Object
	 * @throws
	 */
	public static Object invokeMethod(final Object obj,
			final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("方法名[" + methodName + "] 在 ["
					+ obj + "]中不存在");
		}

		try {
			Object object = method.invoke(obj, args);
			if (object == null) {//自动生成并注入
				object = method.getReturnType().newInstance();
				setFieldValue(obj, StringUtils.uncapitalize(methodName
						.startsWith("set") ? methodName
						.replaceFirst("^set", "") : methodName.replaceFirst(
						"^get", "")), object);
			}
			return object;

		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 调用对象方法,只匹配函数名，如果有多个同名函数调用第一个
	 * 
	 * @Description: TODO
	 * @Title: invokeMethodByName
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param methodName
	 * @param: @param args
	 * @param: @return
	 * @return: Object
	 * @throws
	 */
	public static Object invokeMethodByName(final Object obj,
			final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("方法名[" + methodName + "] 在 ["
					+ obj + "]中不存在");
		}
		try {
			String returntype = method.getReturnType().toString();
			for (int i = 0; i < args.length; i++) {
				if (args[i].getClass().toString().contains("java.lang.String")) {
					if (returntype.contains("java.lang.Double")) {
						args[i] = new Double(args[i].toString());
					} else if (returntype.contains("java.lang.Integer")) {
						args[i] = new Integer(args[i].toString());
					} else if (returntype.contains("java.lang.Float")) {
						args[i] = new Float(args[i].toString());
					} else if (returntype.contains("java.math.BigDecimal")) {
						args[i] = new BigDecimal(args[i].toString());
					} else if (returntype.contains("java.lang.Boolean")) {
						args[i] = new Boolean(args[i].toString());
					} else if (returntype.contains("java.lang.Short")) {
						args[i] = new Short(args[i].toString());
					} else if (returntype.contains("java.lang.Byte")) {
						args[i] = new Byte(args[i].toString());
					} else if (returntype.contains("java.lang.Long")) {
						args[i] = new Long(args[i].toString());
					}
				}
			}
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 获取对象的DeclaredField,并强制设置为可访问,无法找到返回null,
	 * 
	 * @Description: TODO
	 * @Title: getAccessibleMethodByName
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param methodName
	 * @param: @return
	 * @return: Method
	 * @throws
	 */
	public static Field getAccessibleField(final Object obj,
			final String fieldName) {
		Validate.notNull(obj, "对象不能为空");
		Validate.notEmpty(fieldName, "熟属性名不能为空");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {
				continue;
			}
		}
		return null;
	}

	/**
	 * 获取对象的DeclaredMethod,匹配方法名+参数,并强制设置为可访问,无法找到返回null,
	 * 用于方法需要被多次调用的情况,然后调用Method.invoke(...)
	 * 
	 * @Description: TODO
	 * @Title: getAccessibleMethodByName
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param methodName
	 * @param: @return
	 * @return: Method
	 * @throws
	 */
	public static Method getAccessibleMethod(final Object obj,
			final String methodName, final Class<?>... parameterTypes) {
		Validate.notNull(obj, "对象不能为空");
		Validate.notEmpty(methodName, "方法名不能为空");
		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
				.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName,
						parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				continue;
			}
		}
		return null;
	}

	/**
	 * 循环获取对象的DeclaredMethod,只匹配方法名,并强制设置为可访问,无法找到返回null,
	 * 用于方法需要被多次调用的情况,然后调用Method.invoke(...)
	 * 
	 * @Description: TODO
	 * @Title: getAccessibleMethodByName
	 * @author: sunshine
	 * @param: @param obj
	 * @param: @param methodName
	 * @param: @return
	 * @return: Method
	 * @throws
	 */
	public static Method getAccessibleMethodByName(final Object obj,
			final String methodName) {
		Validate.notNull(obj, "对象不能为空");
		Validate.notEmpty(methodName, "方法名不能为空");
		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType
				.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 设置方法可访问
	 * 
	 * @Description: TODO
	 * @Title: makeAccessible
	 * @author: sunshine
	 * @param: @param method
	 * @return: void
	 * @throws
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier
				.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 设置属性可访问
	 * 
	 * @Description: TODO
	 * @Title: makeAccessible
	 * @author: sunshine
	 * @param: @param field
	 * @return: void
	 * @throws
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
					.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * 获得Class定义中声明的父类的第一个泛型参数的类型
	 * 
	 * @Description: TODO
	 * @Title: getClassGenricType
	 * @author: sunshine
	 * @param: @param clazz
	 * @param: @return
	 * @return: Class<T>
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClassGenricType(final Class clazz) {
		return getClassGenericType(clazz, 0);
	}

	/**
	 * 获得Class定义中声明的父类的泛型参数的类型
	 * 
	 * @Description: TODO
	 * @Title: getClassGenricType
	 * @author: sunshine
	 * @param: @param clazz
	 * @param: @param index
	 * @param: @return
	 * @return: Class
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClassGenericType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getName()+"父类不是范型类");
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn(clazz.getName()+"父类范型参数越界");
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getName()+"父类范型参数设置的不是实际类");
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception
	 * 
	 * @Description: TODO
	 * @Title: convertReflectionExceptionToUnchecked
	 * @author: sunshine
	 * @param: @param e
	 * @param: @return
	 * @return: RuntimeException
	 * @throws
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(
			Exception e) {
		if (e instanceof IllegalAccessException
				|| e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(
					((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException(e);
	}
	
	public static Class<?> getOriginalClass(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		Class<?> clazz = instance.getClass();
		if (clazz != null && clazz.getName().contains("$$")) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;

	}

	public static void main(String[] args) {
		// Doctor dt=new Doctor();
		// System.out.println(ReflectionUtils.invokeGetter(dt, "image.webAdd"));
		// ReflectionUtils.invokeSetter(dt, "image.webAdd", "2222");
		// System.out.println(dt.getImage().getWebAdd());

	}
}