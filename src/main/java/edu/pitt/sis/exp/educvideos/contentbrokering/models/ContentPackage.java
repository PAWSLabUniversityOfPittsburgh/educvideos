package edu.pitt.sis.exp.educvideos.contentbrokering.models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentPackage {

	private String name;
	private String url;
	private String title;
	private String description;
	private String author;
	private String license;
	private String version;
	private String created;
	private String modified;
	private List<String> keywords;
	
	public ContentPackage() {}
	public ContentPackage(final String name, final String url, final String title, final String description, final String author, final String license, final String version, final String created, final String modified, final List<String> keywords) {
		this.setName(name);
		this.setUrl(url);
		this.setTitle(title);
		this.setDescription(description);
		this.setAuthor(author);
		this.setLicense(license);
		this.setVersion(version);
		this.setCreated(created);
		this.setModified(modified);
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
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
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
	
}
