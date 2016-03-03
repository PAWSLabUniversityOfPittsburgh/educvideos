package edu.pitt.sis.exp.educvideos.contentbrokering.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentDetailResponse {

	private ContentItemDetail item;
	
	public ContentDetailResponse() {}
	public ContentDetailResponse(final ContentItemDetail item) {
		this.setItem(item);
	}
	
	public ContentItemDetail getItem() {
		return item;
	}
	public void setItem(ContentItemDetail item) {
		this.item = item;
	}
}
