package edu.pitt.sis.exp.educvideos.contentbrokering.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentType {

	private String name;
	private String url;
	private String title;
	private String description;
	
	public ContentType() {}
	public ContentType(final String name, final String url, final String title, final String description) {
		this.setName(name);
		this.setUrl(url);
		this.setTitle(title);
		this.setDescription(description);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
