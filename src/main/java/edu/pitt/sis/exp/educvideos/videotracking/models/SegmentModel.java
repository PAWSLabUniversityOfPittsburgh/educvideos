package edu.pitt.sis.exp.educvideos.videotracking.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SegmentModel {

	private Integer id;
	private Integer start;
	private Integer end;
	
	public SegmentModel() {}
	public SegmentModel(Integer id, Integer start, Integer end) {
		this.setId(id);
		this.setStart(start);
		this.setEnd(end);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getEnd() {
		return end;
	}
	public void setEnd(Integer end) {
		this.end = end;
	}
}
