package edu.pitt.sis.exp.educvideos.videotracking.models;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserActivityModel {

	@XmlElement(name="user-id")
	private String user_id;
	@XmlElement(name="group-id")
	private String group_id;
	@XmlElement(name="content-list-by-provider")
	private List<ContentByProvider> content_list_by_provider;
	
	public UserActivityModel() {
		
	}
	
	public UserActivityModel(final String user_id, final String group_id, final List<ContentByProvider> content_list_by_provider) {
		this.setUser_id(user_id);
		this.setGroup_id(group_id);
		this.setContent_list_by_provider(content_list_by_provider);
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

	public List<ContentByProvider> getContent_list_by_provider() {
		return content_list_by_provider;
	}

	public void setContent_list_by_provider(List<ContentByProvider> content_list_by_provider) {
		this.content_list_by_provider = content_list_by_provider;
	}
	
}
