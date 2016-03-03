package edu.pitt.sis.exp.educvideos.entities;

public class UserActivity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String userId;
	private String groupId;
	private Segment segment;
	private Float progress;
	private Integer attempts;
	private Integer timeSpent;
	private String startToEndTime;

	public UserActivity() {
	}

	public UserActivity(String userId, String groupId, Segment segment,
			Float progress, Integer attempts, Integer timeSpent,
			String startToEndTime) {
		this.setUserId(userId);
		this.setGroupId(groupId);
		this.setSegment(segment);
		this.setProgress(progress);
		this.setAttempts(attempts);
		this.setTimeSpent(timeSpent);
		this.setStartToEndTime(startToEndTime);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
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

	public Integer getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(Integer timeSpent) {
		this.timeSpent = timeSpent;
	}

	public String getStartToEndTime() {
		return startToEndTime;
	}

	public void setStartToEndTime(String startToEndTime) {
		this.startToEndTime = startToEndTime;
	}
}
