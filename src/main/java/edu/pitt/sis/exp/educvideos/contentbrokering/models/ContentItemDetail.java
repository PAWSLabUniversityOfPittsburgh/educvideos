package edu.pitt.sis.exp.educvideos.contentbrokering.models;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentItemDetail {

	private String name;
	private String url;
	@XmlElement(name="html_url")
	private String htmlUrl;
	private String title;
	private String description;
	private String author;
	private String version;
	private String created;
	private String modified;
	private List<String> keywords;
	private List<SubItem> subitems;
	
	public ContentItemDetail() {}

	public ContentItemDetail(final String name, final String url, final String htmlUrl, final String title,
			final String description, final String author, final String version, final String created,
			final String modified, final List<String> keywords, final List<SubItem> subItems) {
		super();
		this.name = name;
		this.url = url;
		this.htmlUrl = htmlUrl;
		this.title = title;
		this.description = description;
		this.author = author;
		this.version = version;
		this.created = created;
		this.modified = modified;
		this.keywords = keywords;
		this.subitems = subItems;
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

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<SubItem> getSubitems() {
		return subitems;
	}

	public void setSubitems(List<SubItem> subitems) {
		this.subitems = subitems;
	}
}
