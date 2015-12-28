package com.platform.modules.sns.bean;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.platform.common.persistence.DataEntity;

/**
 * 圈子Entity
 * @author sunshine
 * @date 2015-12-25
 */
public class Sns extends DataEntity<Sns> {
	
	private static final long serialVersionUID = 1L;
	private String snsid;		// 主键
	private String snsname;		// 圈子名称
	private String snsphotoid;		// 圈子配图ID
	private String description;		// 简介
	private Date createdate;		// 创建时间
	private String createuserid;		// 圈子创建人Id
	private String snssubject;		// 圈子主题，标题
	private String categoryid;		// 圈子类别ID
	private String defaults;		// 是否默认拥有的圈子  为true时表示孕妇注册时自动绑定到我的圈。
	private String sort;		// 排序
	private String recommend;		// 是否推荐，为true时显示到我的圈里面。
	private String attentioncount;		// 关注人数
	private String postcount;		// 帖子数量
	private String todaypostcount;		// 今日帖子数目
	
	public Sns() {
		super();
	}

	public Sns(String id){
		super(id);
	}

	@Length(min=1, max=32, message="主键长度必须介于 1 和 32 之间")
	public String getSnsid() {
		return snsid;
	}

	public void setSnsid(String snsid) {
		this.snsid = snsid;
	}
	
	@Length(min=1, max=200, message="圈子名称长度必须介于 1 和 200 之间")
	public String getSnsname() {
		return snsname;
	}

	public void setSnsname(String snsname) {
		this.snsname = snsname;
	}
	
	@Length(min=0, max=32, message="圈子配图ID长度必须介于 0 和 32 之间")
	public String getSnsphotoid() {
		return snsphotoid;
	}

	public void setSnsphotoid(String snsphotoid) {
		this.snsphotoid = snsphotoid;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@Length(min=0, max=32, message="圈子创建人Id长度必须介于 0 和 32 之间")
	public String getCreateuserid() {
		return createuserid;
	}

	public void setCreateuserid(String createuserid) {
		this.createuserid = createuserid;
	}
	
	@Length(min=0, max=200, message="圈子主题，标题长度必须介于 0 和 200 之间")
	public String getSnssubject() {
		return snssubject;
	}

	public void setSnssubject(String snssubject) {
		this.snssubject = snssubject;
	}
	
	@Length(min=0, max=32, message="圈子类别ID长度必须介于 0 和 32 之间")
	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	
	@Length(min=0, max=2, message="是否默认拥有的圈子  为true时表示孕妇注册时自动绑定到我的圈。长度必须介于 0 和 2 之间")
	public String getDefaults() {
		return defaults;
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}
	
	@Length(min=0, max=11, message="排序长度必须介于 0 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	@Length(min=0, max=2, message="是否推荐，为true时显示到我的圈里面。长度必须介于 0 和 2 之间")
	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	
	@Length(min=0, max=11, message="关注人数长度必须介于 0 和 11 之间")
	public String getAttentioncount() {
		return attentioncount;
	}

	public void setAttentioncount(String attentioncount) {
		this.attentioncount = attentioncount;
	}
	
	@Length(min=0, max=11, message="帖子数量长度必须介于 0 和 11 之间")
	public String getPostcount() {
		return postcount;
	}

	public void setPostcount(String postcount) {
		this.postcount = postcount;
	}
	
	@Length(min=0, max=11, message="今日帖子数目长度必须介于 0 和 11 之间")
	public String getTodaypostcount() {
		return todaypostcount;
	}

	public void setTodaypostcount(String todaypostcount) {
		this.todaypostcount = todaypostcount;
	}
	
}