package edu.pitt.sis.exp.educvideos.videotracking.models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VideoResponseModel {

	private String youtubeId;
	private Integer startTime;
	private Integer endTime;
	private Boolean validUser;
	private List<SegmentModel> segments;
	
	public VideoResponseModel() {}
	public VideoResponseModel(final String youtubeId, final Integer startTime, final Integer endTime, final Boolean validUser, final List<SegmentModel> segments) {
		this.youtubeId = youtubeId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.setValidUser(validUser);
		this.setSegments(segments);
	}
	
	public String getYoutubeId() {
		return youtubeId;
	}
	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}
	public Integer getStartTime() {
		return startTime;
	}
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
	public Integer getEndTime() {
		return endTime;
	}
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public Boolean getValidUser() {
		return validUser;
	}
	public void setValidUser(Boolean validUser) {
		this.validUser = validUser;
	}
	public List<SegmentModel> getSegments() {
		return segments;
	}
	public void setSegments(List<SegmentModel> segments) {
		this.segments = segments;
	}
}
