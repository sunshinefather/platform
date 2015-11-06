package com.platform.modules.sys.security;
/**
 * 用户名,密码和验证码  认证类
 * @ClassName:  UsernamePasswordToken   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月6日 下午2:15:27
 */
public class UPCToken extends org.apache.shiro.authc.UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;
	private boolean mobileLogin;
	
	public UPCToken() {
		super();
	}

	public UPCToken(String username,String password,boolean rememberMe,String host,String captcha,boolean mobileLogin) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
		this.mobileLogin = mobileLogin;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public boolean isMobileLogin() {
		return mobileLogin;
	}
	
}