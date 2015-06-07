package com.rpframework.website.edongwang.domain;

import com.rpframework.core.Domain;
import com.rpframework.core.mybatis.plugin.annotation.FieldMapperAnnotation;
import com.rpframework.core.mybatis.plugin.annotation.FieldType;
import com.rpframework.core.mybatis.plugin.annotation.TableMapperAnnotation;
import com.rpframework.core.mybatis.plugin.annotation.UniqueKeyType;

/**
 * 
 * title: UserSalesman.java 
 * description
 *
 * @author rplees
 * @email rplees.i.ly@gmail.com
 * @version 1.0  
 * @created 2015年6月7日 下午5:32:21
 */
@TableMapperAnnotation(tableName = "e_user_salesman", uniqueKeyType = UniqueKeyType.Single, uniqueKey = "userId") 
public class UserSalesman extends Domain {
	
	/**描述*/  
	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation
	Integer userId;
	
	@FieldMapperAnnotation
	String credentialsImg; //证卷名称
	@FieldMapperAnnotation(dbFieldName="houseId", fieldType=FieldType.Object)
	House house; //楼盘ID
	@FieldMapperAnnotation
	Integer isLeader;
	@FieldMapperAnnotation
	Integer state;
	@FieldMapperAnnotation
	Long recordCreateTime;
	@FieldMapperAnnotation
	Long recordModifyTime;

	public Integer getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(Integer isLeader) {
		this.isLeader = isLeader;
	}

	public Integer getUserId() {
		return userId;
	}

	public Long getRecordCreateTime() {
		return recordCreateTime;
	}

	public void setRecordCreateTime(Long recordCreateTime) {
		this.recordCreateTime = recordCreateTime;
	}

	public Long getRecordModifyTime() {
		return recordModifyTime;
	}

	public void setRecordModifyTime(Long recordModifyTime) {
		this.recordModifyTime = recordModifyTime;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCredentialsImg() {
		return credentialsImg;
	}

	public void setCredentialsImg(String credentialsImg) {
		this.credentialsImg = credentialsImg;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
}
