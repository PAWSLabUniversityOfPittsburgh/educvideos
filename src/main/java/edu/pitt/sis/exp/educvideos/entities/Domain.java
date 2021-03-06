package edu.pitt.sis.exp.educvideos.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain generated by hbm2java
 */
public class Domain implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String title;
	private String description;
	private User author;
	private String license;
	private String version;
	private Date created;
	private Date modified;
	private Set<Topic> topics = new HashSet<Topic>(0);

	public Domain() {

	}

	public Domain(String name, String title, String description, User author,
			String license, String version, Date created, Date modified) {
		this.setName(name);
		this.setTitle(title);
		this.setDescription(description);
		this.setAuthor(author);
		this.setLicense(license);
		this.setVersion(version);
		this.setCreated(created);
		this.setModified(modified);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

}
