package edu.pitt.sis.exp.educvideos.videotracking.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VideoModel {

	private String videoid;
	private String sub;
	private String usr;
	private String grp;
	private String sid;
	
	public VideoModel() {}
	public VideoModel(final String videoid, final String sub, final String usr, final String grp, final String sid) {
		this.setVideoid(videoid);
		this.setSub(sub);
		this.setUsr(usr);
		this.setGrp(grp);
		this.setSid(sid);
	}
	public String getVideoid() {
		return videoid;
	}
	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getUsr() {
		return usr;
	}
	public void setUsr(String usr) {
		this.usr = usr;
	}
	public String getGrp() {
		return grp;
	}
	public void setGrp(String grp) {
		this.grp = grp;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
}
