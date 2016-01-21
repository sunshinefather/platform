package com.platform.modules.sys.bean;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import org.hibernate.validator.constraints.Length;
import com.platform.common.persistence.TreeEntity;

/**
 * 多层级字典Entity 
 * @author sunshine
 */
public class MultiDict extends TreeEntity<MultiDict> {

	private static final long serialVersionUID = 1L;
	
	/**数据值*/
	private String value;
	/**编码*/
	private String code;

	public MultiDict() {
		super();
	}
	
	public MultiDict(String id){
		super(id);
	}
	
	public MultiDict(String value, String name){
		this.value = value;
		this.name = name;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public MultiDict getParent() {
		return parent;
	}

	@Override
	public void setParent(MultiDict parent) {
		this.parent=parent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}