package edu.pitt.sis.exp.educvideos.videotracking.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentModel {

	@XmlElement(name="content-id")
	private String content_id;
	private Float progress;
	private Integer attempts;
	@XmlElement(name="success-rate")
	private Integer success_rate;
	@XmlElement(name="annotation-count")
	private Integer annotation_count;
	@XmlElement(name="like-count")
	private Integer like_count;
	@XmlElement(name="time-spent")
	private Integer time_spent;
	@XmlElement(name="sub-activities")
	private Integer sub_activities;
	
	public ContentModel() {
		
	}
	
	public ContentModel(String content_id, Float progress, Integer attempts, Integer success_rate, Integer annotation_count, Integer like_count, Integer time_spent,
			Integer sub_activities) {
		this.setContent_id(content_id);
		this.setProgress(progress);
		this.setAttempts(attempts);
		this.setSuccess_rate(success_rate);
		this.setAnnotation_count(annotation_count);
		this.setLike_count(like_count);
		this.setTime_spent(time_spent);
		this.setSub_activities(sub_activities);
	}

	public String getContent_id() {
		return content_id;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public Float getProgress() {
		return progress;
	}

	public void setProgress(Float progress) {
		this.progress = progress;
	}

	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public Integer getSuccess_rate() {
		return success_rate;
	}

	public void setSuccess_rate(Integer success_rate) {
		this.success_rate = success_rate;
	}

	public Integer getAnnotation_count() {
		return annotation_count;
	}

	public void setAnnotation_count(Integer annotation_count) {
		this.annotation_count = annotation_count;
	}

	public Integer getLike_count() {
		return like_count;
	}

	public void setLike_count(Integer like_count) {
		this.like_count = like_count;
	}

	public Integer getTime_spent() {
		return time_spent;
	}

	public void setTime_spent(Integer time_spent) {
		this.time_spent = time_spent;
	}

	public Integer getSub_activities() {
		return sub_activities;
	}

	public void setSub_activities(Integer sub_activities) {
		this.sub_activities = sub_activities;
	}
	
}
