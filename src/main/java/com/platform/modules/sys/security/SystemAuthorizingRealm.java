package com.platform.modules.sys.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;
import com.platform.common.config.Global;
import com.platform.common.servlet.ValidateCodeServlet;
import com.platform.common.utils.Encodes;
import com.platform.common.web.ServletUtils;
import com.platform.modules.sys.bean.Menu;
import com.platform.modules.sys.bean.Role;
import com.platform.modules.sys.bean.User;
import com.platform.modules.sys.controller.LoginController;
import com.platform.modules.sys.service.SystemService;
import com.platform.modules.sys.utils.LogUtils;
import com.platform.modules.sys.utils.UserUtils;

/**
 * shiro 提供系统安全数据(用户、角色、权限)
 * @ClassName:  SystemAuthorizingRealm   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年11月6日 晚上2:10:01
 */
@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {
	
	@Resource
	private SystemService systemService;
	
    /**
     * 认证回调函数,登录时调用
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UPCToken token = (UPCToken) authcToken;	
		
		// 校验登录验证码
		if (LoginController.isValidateCodeLogin(token.getUsername(), false, false)){
			Session session = UserUtils.getSession();
			String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
			if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)){
				throw new AuthenticationException("msg:验证码错误, 请重试");
			}
		}
		
		// 校验用户名密码
		User user = systemService.getUserByLoginName(token.getUsername());
		if (user != null) {
			if (Global.NO.equals(user.getLoginFlag())){
				throw new AuthenticationException("msg:该已帐号禁止登录");
			}
			String userPwd=user.getPassword();
			byte[] salt = Encodes.decodeHex(userPwd.substring(0,16));
			return new SimpleAuthenticationInfo(new Principal(user,token.isMobileLogin()),userPwd.substring(16), ByteSource.Util.bytes(salt),getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		// 获取当前已登录的用户
		if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))){
			Collection<Session> sessions = systemService.getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
			if (sessions.size() > 0){
				// 如果是登录进来的，则踢出已在线用户
				if (UserUtils.getSubject().isAuthenticated()){
					for (Session session : sessions){
						systemService.getSessionDao().delete(session);
					}
				}
				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
				else{
					UserUtils.getSubject().logout();
					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
				}
			}
		}
		User user = systemService.getUserByLoginName(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list){
				if (StringUtils.isNotBlank(menu.getPermission())){
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermission(),",")){
						info.addStringPermission(permission);
					}
				}
			}
			
			// 添加用户权限
			info.addStringPermission("user");
			
			// 添加用户角色信息
			for (Role role : user.getRoleList()){
				info.addRole(role.getEnname());
			}
			
			// 更新登录IP和时间
			systemService.updateUserLoginInfo(user);
			
			// 记录登录日志
			LogUtils.saveLog(ServletUtils.getRequest(), "系统登录");
			return info;
		} else {
			return null;
		}
	}
	
	@Override
	protected void checkPermission(Permission permission, AuthorizationInfo info) {
		authorizationValidate(permission);
		super.checkPermission(permission, info);
	}
	
	@Override
	protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
        		authorizationValidate(permission);
            }
        }
		return super.isPermitted(permissions, info);
	}
	
	@Override
	public boolean isPermitted(PrincipalCollection principals, Permission permission) {
		authorizationValidate(permission);
		return super.isPermitted(principals, permission);
	}
	
	@Override
	protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
            	authorizationValidate(permission);
            }
        }
		return super.isPermittedAll(permissions, info);
	}
	
	/**
	 * 授权验证方法
	 */
	private void authorizationValidate(Permission permission){
		//模块授权预留接口
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id; //id
		private String loginName; // 登录名
		private String name; //真实姓名
		private boolean mobileLogin; //手机登录

		public Principal(User user, boolean mobileLogin) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
			this.mobileLogin = mobileLogin;
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public boolean isMobileLogin() {
			return mobileLogin;
		}

		public String getSessionid() {
			try{
				return (String) UserUtils.getSession().getId();
			}catch (Exception e) {
				return "";
			}
		}
		
		@Override
		public String toString() {
			return id;
		}

	}
}