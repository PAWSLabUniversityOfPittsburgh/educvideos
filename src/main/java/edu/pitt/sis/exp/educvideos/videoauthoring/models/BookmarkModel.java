package edu.pitt.sis.exp.educvideos.videoauthoring.models;

import java.util.List;

public class BookmarkModel {

	private Integer id;
	private String videoTitle;
	private String youtubeId;
	private String query;
	private String notes;
	private Boolean addedToLibrary;
	private List<Integer> segmentIds;
	private List<KeywordModel> keywords;	
	
	public BookmarkModel() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public String getYoutubeId() {
		return youtubeId;
	}

	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getAddedToLibrary() {
		return addedToLibrary;
	}

	public void setAddedToLibrary(Boolean addedToLibrary) {
		this.addedToLibrary = addedToLibrary;
	}

	public List<Integer> getSegmentIds() {
		return segmentIds;
	}

	public void setSegmentIds(List<Integer> segmentIds) {
		this.segmentIds = segmentIds;
	}

	public List<KeywordModel> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordModel> keywords) {
		this.keywords = keywords;
	}
	
}
