package edu.pitt.sis.exp.educvideos.videotracking.models;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserActivityResponseModel {

	@XmlElement(name="user-id")
	private String user_id;
	@XmlElement(name="group-id")
	private String group_id;
	@XmlElement(name="content-list")
	private List<ContentModel> content_list;
	
	public UserActivityResponseModel() {
		
	}
	
	public UserActivityResponseModel(String user_id, String group_id, List<ContentModel> content_list) {
		this.setUser_id(user_id);
		this.setGroup_id(group_id);
		this.setContent_list(content_list);
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public List<ContentModel> getContent_list() {
		return content_list;
	}

	public void setContent_list(List<ContentModel> content_list) {
		this.content_list = content_list;
	}
}
