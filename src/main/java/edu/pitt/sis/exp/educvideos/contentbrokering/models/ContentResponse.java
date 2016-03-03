package edu.pitt.sis.exp.educvideos.contentbrokering.models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentResponse {

	private List<ContentItem> content;
	
	public ContentResponse() {}
	public ContentResponse(final List<ContentItem> content) {
		this.setContent(content);
	}
	
	public List<ContentItem> getContent() {
		return content;
	}
	public void setContent(List<ContentItem> content) {
		this.content = content;
	}
	
}
