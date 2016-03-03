package edu.pitt.sis.exp.educvideos.videoauthoring.models;

import java.util.List;

public class SearchVideoModel {

	private String youtubeId;
	private String title;
	private String description;
	private String publishedAt;
	private String thumbnailUrl;
	private Boolean addedToBookmarks;
	private Integer bookmarkId;
	private Boolean addedToLibrary;
	private List<Integer> segmentIds;
	private String duration;
	
	public SearchVideoModel(){}
	
	public String getYoutubeId() {
		return youtubeId;
	}

	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
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

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public Boolean getAddedToBookmarks() {
		return addedToBookmarks;
	}

	public void setAddedToBookmarks(Boolean addedToBookmarks) {
		this.addedToBookmarks = addedToBookmarks;
	}

	public Integer getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(Integer bookmarkId) {
		this.bookmarkId = bookmarkId;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
