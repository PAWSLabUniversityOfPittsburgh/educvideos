package edu.pitt.sis.exp.educvideos.entities;

public class Bookmark implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Video video;
	private String query;
	private User author;
	private String notes;
	
	public Bookmark(){}
	public Bookmark(Video video, String query, User author, String notes) {
		this.setVideo(video);
		this.setQuery(query);
		this.setAuthor(author);
		this.setNotes(notes);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Video getVideo() {
		return video;
	}
	public void setVideo(Video video) {
		this.video = video;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
