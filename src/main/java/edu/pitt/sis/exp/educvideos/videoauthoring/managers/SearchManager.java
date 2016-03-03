package edu.pitt.sis.exp.educvideos.videoauthoring.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.client.util.DateTime;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.BookmarkDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.SegmentDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Bookmark;
import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.SearchResponseModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.SearchVideoModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.search.Search;

public class SearchManager {

	public SearchResponseModel getSearchResponse(String query, String date,
			String page, String duration, String username) throws Throwable {
		try {
			//get results from YouTube
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			DateTime publishedAfter = new DateTime(d);
			if(page.trim().length()==0)
				page = null;
			Search searchObj = new Search();
			SearchResponseModel searchResponseModel = searchObj.getVideos(query, publishedAfter, page, duration);
			
			//check if the results exist in bookmarks; if yes, then include the bookmark id
			BookmarkDaoImpl bookmarkDaoImpl = new BookmarkDaoImpl();
			List<Bookmark> bookmarks = bookmarkDaoImpl.getAllBookmarksByAuthor(username);
			for(Bookmark bookmark: bookmarks) { //loop through each bookmark of the user
				for(SearchVideoModel searchVideoModel: searchResponseModel.getSearchVideoModelList()) { //loop through each search video
					if(bookmark.getVideo().getYoutubeId().equals(searchVideoModel.getYoutubeId())) { //if both the youtube ids  match
						searchVideoModel.setAddedToBookmarks(true); //search video exists in bookmark
						searchVideoModel.setBookmarkId(bookmark.getId()); //set the bookmark id for viewing the bookmark
					}
				}
			}
			
			//check if the results exist in library; if yes then include all the segment ids
			SegmentDaoImpl segmentDaoImpl = new SegmentDaoImpl();
			List<Segment> segments = segmentDaoImpl.getAllSegments();
			for(Segment segment: segments) {
				for(SearchVideoModel searchVideoModel: searchResponseModel.getSearchVideoModelList()) { //loop through each search video
					if(segment.getVideo().getYoutubeId().equals(searchVideoModel.getYoutubeId())) { //if both the youtube ids match
						searchVideoModel.setAddedToLibrary(true); //search video exists in library
						searchVideoModel.getSegmentIds().add(segment.getId()); //add the segment id into the list of segment ids for viewing all segments of search video
					}
				}
			}
			
			return searchResponseModel;
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}

	public SearchResponseModel getSearchResponseById(String youtubeid, String username) throws Throwable {
		try {
			Search searchObj = new Search();
			SearchResponseModel searchResponseModel = searchObj.getVideoById(youtubeid);
			//check if the results exist in bookmarks; if yes, then include the bookmark id
			BookmarkDaoImpl bookmarkDaoImpl = new BookmarkDaoImpl();
			Bookmark bookmark = bookmarkDaoImpl.getBookmarkByAuthorAndYoutubeId(username, youtubeid);
			if(bookmark!=null && bookmark.getId()!=null) {
				List<SearchVideoModel> searchVideoModelList = searchResponseModel.getSearchVideoModelList();
				if(searchVideoModelList!=null && searchVideoModelList.size()>0) {
					searchVideoModelList.get(0).setAddedToBookmarks(true);
					searchVideoModelList.get(0).setBookmarkId(bookmark.getId());
				}
			}
			
			//check if the results exist in library; if yes then include all the segment ids
			SegmentDaoImpl segmentDaoImpl = new SegmentDaoImpl();
			List<Segment> segments = segmentDaoImpl.getSegmentsByYoutubeId(youtubeid);
			if(segments != null && segments.size()>0) {
				List<SearchVideoModel> searchVideoModelList = searchResponseModel.getSearchVideoModelList();
				if(searchVideoModelList!=null && searchVideoModelList.size()>0) {
					searchVideoModelList.get(0).setAddedToLibrary(true);
					List<Integer> segmentIds = new ArrayList<Integer>();
					searchVideoModelList.get(0).setSegmentIds(segmentIds);
					for(Segment segment: segments) {
						segmentIds.add(segment.getId());
					}
				}
			}
			
			return searchResponseModel;
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
