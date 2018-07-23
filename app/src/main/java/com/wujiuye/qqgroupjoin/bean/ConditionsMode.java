package com.wujiuye.qqgroupjoin.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 筛选条件
 * @author wjy
 *
 */
public class ConditionsMode implements Serializable {

	private static final long serialVersionUID = 1L;

	// 加群关键词
	private List<String> joinGroupKeywords = new ArrayList<String>();
	// 加群个人简介
	private String joinGroupMyDesc = "哈喽！我是加群机器人！棒棒哒。。。";
	// 每个关键词加群数量，当搜索结果少于加群数量时，只加搜索结果数量
	private int eachKeywordJoinGroupNumber = 10;
	// 只加人数大于多少的群
	private int groupPeopleOfNumber = 100;

	public List<String> getJoinGroupKeywords() {
		return joinGroupKeywords;
	}

	public void setJoinGroupKeywords(List<String> joinGroupKeywords) {
		this.joinGroupKeywords = joinGroupKeywords;
	}

	public String getJoinGroupMyDesc() {
		return joinGroupMyDesc;
	}

	public void setJoinGroupMyDesc(String joinGroupMyDesc) {
		this.joinGroupMyDesc = joinGroupMyDesc;
	}

	public int getEachKeywordJoinGroupNumber() {
		return eachKeywordJoinGroupNumber;
	}

	public void setEachKeywordJoinGroupNumber(int eachKeywordJoinGroupNumber) {
		this.eachKeywordJoinGroupNumber = eachKeywordJoinGroupNumber;
	}

	public int getGroupPeopleOfNumber() {
		return groupPeopleOfNumber;
	}

	public void setGroupPeopleOfNumber(int groupPeopleOfNumber) {
		this.groupPeopleOfNumber = groupPeopleOfNumber;
	}

}
