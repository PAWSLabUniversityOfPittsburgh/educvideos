package edu.pitt.sis.exp.educvideos.videoauthoring.models;

import java.util.List;

public class SearchResponseModel {

	private List<SearchVideoModel> searchVideoModelList;
	private String nextPageToken;
	private String prevPageToken;
	
	public SearchResponseModel() {}
	
	public List<SearchVideoModel> getSearchVideoModelList() {
		return searchVideoModelList;
	}

	public void setSearchVideoModelList(List<SearchVideoModel> searchVideoModelList) {
		this.searchVideoModelList = searchVideoModelList;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public String getPrevPageToken() {
		return prevPageToken;
	}

	public void setPrevPageToken(String prevPageToken) {
		this.prevPageToken = prevPageToken;
	}
	
}
