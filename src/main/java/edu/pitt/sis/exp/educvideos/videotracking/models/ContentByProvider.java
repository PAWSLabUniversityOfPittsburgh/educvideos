package edu.pitt.sis.exp.educvideos.videotracking.models;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentByProvider {

	@XmlElement(name="provider-id")
	private String provider_id;
	@XmlElement(name="content-list")
	private List<String> content_list;
	
	public ContentByProvider() {
		
	}
	
	public ContentByProvider(final String provider_id, final List<String> content_list) {
		this.setProvider_id(provider_id);
		this.setContent_list(content_list);
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}

	public List<String> getContent_list() {
		return content_list;
	}

	public void setContent_list(List<String> content_list) {
		this.content_list = content_list;
	}
}
