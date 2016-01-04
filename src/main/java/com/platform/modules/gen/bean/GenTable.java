package com.platform.modules.gen.bean;

import java.util.List;
import org.hibernate.validator.constraints.Length;
import com.google.common.collect.Lists;
import com.platform.common.persistence.DataEntity;
import com.platform.common.utils.StringUtils;

/**
 * 表描述
 * @author sunshine
 * @date 2015-10-15
 */
public class GenTable extends DataEntity<GenTable> {
	
	private static final long serialVersionUID = 1L;
	/**名称*/
	private String name;
	/**描述*/
	private String comments;
	/**实体类名称*/
	private String className;
	/**父表*/
	private String parentTable;
	/**父表外键*/
	private String parentTableFk;
    /**表字段集合*/
	private List<GenTableColumn> columnList = Lists.newArrayList();
    /**名称模糊查询*/
	private String nameLike;
	/**表主键列表*/
	private List<String> pkList;
	/**父表对象*/
	private GenTable parent;
	/**子表列表*/
	private List<GenTable> childList = Lists.newArrayList();
	
	public GenTable() {
		super();
	}

	public GenTable(String id){
		super(id);
	}

	@Length(min=1, max=200)
	public String getName() {
		return StringUtils.lowerCase(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getParentTable() {
		return StringUtils.lowerCase(parentTable);
	}

	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}

	public String getParentTableFk() {
		return StringUtils.lowerCase(parentTableFk);
	}

	public void setParentTableFk(String parentTableFk) {
		this.parentTableFk = parentTableFk;
	}

	public List<String> getPkList() {
		return pkList;
	}

	public void setPkList(List<String> pkList) {
		this.pkList = pkList;
	}

	public String getNameLike() {
		return nameLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public GenTable getParent() {
		return parent;
	}

	public void setParent(GenTable parent) {
		this.parent = parent;
	}

	public List<GenTableColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<GenTableColumn> columnList) {
		this.columnList = columnList;
	}

	public List<GenTable> getChildList() {
		return childList;
	}

	public void setChildList(List<GenTable> childList) {
		this.childList = childList;
	}
	
	/**
	 * 获取列名和说明
	 * @return
	 */
	public String getNameAndComments() {
		return getName() + (comments == null ? "" : "  :  " + comments);
	}

	/**
	 * 依赖包列表
	 */
	public List<String> getImportList(){
		List<String> importList = Lists.newArrayList();
		for (GenTableColumn column : getColumnList()){
			if (column.getIsNotBaseField() || 
					        ("1".equals(column.getIsQuery()) && "between".equals(column.getQueryType()) && 
					          ("createDate".equals(column.getSimpleJavaField()) || "updateDate".equals(column.getSimpleJavaField()))
							)
			   ){
				//如果类型中包含"."，则需要导入依赖包
				if (StringUtils.indexOf(column.getJavaType(), ".") != -1 && !importList.contains(column.getJavaType())){
					importList.add(column.getJavaType());
				}
			}
			if (column.getIsNotBaseField()){
				for (String ann : column.getAnnotationList()){
					if (!importList.contains(StringUtils.substringBeforeLast(ann, "("))){
						importList.add(StringUtils.substringBeforeLast(ann, "("));
					}
				}
			}
		}
		// 如果有子表，则需要导入List相关引用
		if (getChildList() != null && getChildList().size() > 0){
			if (!importList.contains("java.util.List")){
				importList.add("java.util.List");
			}
			if (!importList.contains("com.google.common.collect.Lists")){
				importList.add("com.google.common.collect.Lists");
			}
		}
		return importList;
	}
	
	/**
	 * 是否存在父类
	 */
	public Boolean getParentExists(){
		return parent != null && StringUtils.isNotBlank(parentTable) && StringUtils.isNotBlank(parentTableFk);
	}

	/**
	 * 是否存在create_date列
	 */
	public Boolean getCreateDateExists(){
		for (GenTableColumn c : columnList){
			if ("create_date".equals(c.getName())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否存在update_date列
	 */
	public Boolean getUpdateDateExists(){
		for (GenTableColumn c : columnList){
			if ("update_date".equals(c.getName())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否存在del_flag列
	 */
	public Boolean getDelFlagExists(){
		for (GenTableColumn c : columnList){
			if ("del_flag".equals(c.getName())){
				return true;
			}
		}
		return false;
	}
}