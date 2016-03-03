package edu.pitt.sis.exp.educvideos.videotracking.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserModel {

	private String user_id;
	private String grp_id;
	private String video_id;
	private String segment_id;
	private Float res;
	private Integer time_watched;
	private Integer start_time;
	private Integer end_time;
	
	public UserModel(){}
	public UserModel(String user_id, String grp_id, String video_id, String segment_id, Float res, Integer time_watched, Integer start_time, Integer end_time) {
		this.setUser_id(user_id);
		this.setGrp_id(grp_id);
		this.setVideo_id(video_id);
		this.setSegment_id(segment_id);
		this.setRes(res);
		this.setTime_watched(time_watched);
		this.setStart_time(start_time);
		this.setEnd_time(end_time);
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getGrp_id() {
		return grp_id;
	}
	public void setGrp_id(String grp_id) {
		this.grp_id = grp_id;
	}
	public String getVideo_id() {
		return video_id;
	}
	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}
	public String getSegment_id() {
		return segment_id;
	}
	public void setSegment_id(String segment_id) {
		this.segment_id = segment_id;
	}
	public Float getRes() {
		return res;
	}
	public void setRes(Float res) {
		this.res = res;
	}
	public Integer getTime_watched() {
		return time_watched;
	}
	public void setTime_watched(Integer time_watched) {
		this.time_watched = time_watched;
	}
	public Integer getStart_time() {
		return start_time;
	}
	public void setStart_time(Integer start_time) {
		this.start_time = start_time;
	}
	public Integer getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Integer end_time) {
		this.end_time = end_time;
	}
	
}
