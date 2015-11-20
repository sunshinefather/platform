package com.platform.common.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.DefaultResourceLoader;
import com.ckfinder.connector.ServletContextFactory;
import com.google.common.collect.Maps;
import com.platform.common.utils.PropertiesLoader;
import com.platform.common.utils.StringUtils;
/**
 * 全局配置类
 * @ClassName:  Global   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月5日 下午2:19:52
 */
public class Global {

	private static Global global = new Global();
	private static Map<String, String> map = Maps.newHashMap();
	private static PropertiesLoader loader = new PropertiesLoader("project-config.properties");
	
	public static final String SHOW = "1";
	public static final String HIDE = "0";
	
	public static final String YES = "1";
	public static final String NO = "0";
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final String USERFILES_BASE_URL = "/userfiles/";
	
	public static Global getInstance() {
		return global;
	}
	
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
	
	public static String getConfig(String key,String defaultValue) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key,defaultValue);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
	
	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	
	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	
	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}
    
	/**
	 * 页面获取常量
	 * @see ${fns:getConst('YES')}
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取上传文件的根目录
	 * @return
	 */
	public static String getUserfilesBaseDir() {
		String dir = getConfig("userfiles.basedir");
		if (StringUtils.isBlank(dir)){
			try {
				dir = ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e) {
				return "";
			}
		}
		if(!dir.endsWith("/")) {
			dir += "/";
		}
		return dir;
	}
	
    /**
     * 获取工程路径
     * @return
     */
    public static String getProjectPath(){
		String projectPath = Global.getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)){
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null){
				while(true){
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()){
						break;
					}
					if (file.getParentFile() != null){
						file = file.getParentFile();
					}else{
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
    }
	
}