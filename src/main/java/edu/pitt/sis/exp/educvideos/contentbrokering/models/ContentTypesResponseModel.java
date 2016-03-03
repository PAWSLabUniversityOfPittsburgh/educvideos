package edu.pitt.sis.exp.educvideos.contentbrokering.models;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentTypesResponseModel {

	private List<ContentType> contentTypes;
	
	public ContentTypesResponseModel() {}
	public ContentTypesResponseModel(final List<ContentType> contentTypes) {
		this.setContentTypes(contentTypes);
	}
	
	public List<ContentType> getContentTypes() {
		return contentTypes;
	}
	public void setContentTypes(List<ContentType> contentTypes) {
		this.contentTypes = contentTypes;
	}
	
}
