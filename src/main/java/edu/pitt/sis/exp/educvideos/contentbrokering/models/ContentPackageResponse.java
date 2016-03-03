package edu.pitt.sis.exp.educvideos.contentbrokering.models;

import java.util.List;

public class ContentPackageResponse {

	private List<ContentPackage> contentPackages;
	
	public ContentPackageResponse() {}
	public ContentPackageResponse(final List<ContentPackage> contentPackages) {
		this.setContentPackages(contentPackages);
	}
	public List<ContentPackage> getContentPackages() {
		return contentPackages;
	}
	public void setContentPackages(List<ContentPackage> contentPackages) {
		this.contentPackages = contentPackages;
	}
}
