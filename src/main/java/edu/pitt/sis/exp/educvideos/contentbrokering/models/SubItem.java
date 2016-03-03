package edu.pitt.sis.exp.educvideos.contentbrokering.models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubItem {

	private String name;
	private String url;
	private List<String> keywords;
	
	public SubItem() {}
	public SubItem(final String name, final String url, final List<String> keywords) {
		super();
		this.setName(name);
		this.setUrl(url);
		this.setKeywords(keywords);
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
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
}
