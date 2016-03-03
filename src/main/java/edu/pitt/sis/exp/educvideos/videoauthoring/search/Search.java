/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package edu.pitt.sis.exp.educvideos.videoauthoring.search;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import edu.pitt.sis.exp.educvideos.videoauthoring.models.SearchResponseModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.SearchVideoModel;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Prints a list of videos based on a search term.
 *
 * @author Jeremy Walker
 */
public class Search {

  /** Global instance properties filename. */
  private static String PROPERTIES_FILENAME = "youtube.properties";

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
  private static final long NUMBER_OF_VIDEOS_RETURNED = 12;

  /** Global instance of Youtube object to make all API requests. */
  private static YouTube youtube;


  /**
   * Initializes YouTube object to search for videos on YouTube (Youtube.Search.List). The program
   * then prints the names and thumbnails of each of the videos (only first 50 videos).
   *
   * @param query to retrieve videos from YouTube
   */
  public SearchResponseModel getVideos(String query, DateTime publishedAfter, String pageToken, String duration) throws Exception, Throwable {
    // Read the developer key from youtube.properties
    Properties properties = new Properties();
    try {
      InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
      properties.load(in);

    } catch (IOException e) {
      System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
          + " : " + e.getMessage());
      System.exit(1);
    }
    SearchResponseModel searchResponseModel = new SearchResponseModel();
    List<SearchVideoModel> videoModelList = new ArrayList<SearchVideoModel>();
    try {
      /*
       * The YouTube object is used to make all API requests. The last argument is required, but
       * because we don't need anything initialized when the HttpRequest is initialized, we override
       * the interface and provide a no-op function.
       */
      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
        public void initialize(HttpRequest request) throws IOException {}
      }).setApplicationName("youtube-cmdline-search-sample").build();

      // Get query term from user.
      //String queryTerm = getInputQuery();

      YouTube.Search.List search = youtube.search().list("id,snippet");

      /*
       * It is important to set your developer key from the Google Developer Console for
       * non-authenticated requests (found under the API Access tab at this link:
       * code.google.com/apis/). This is good practice and increased your quota.
       */
      String apiKey = properties.getProperty("youtube.apikey");
      search.setKey(apiKey);
      search.setQ(query + " tutorial");
      /*
       * We are only searching for videos (not playlists or channels). If we were searching for
       * more, we would add them as a string like this: "video,playlist,channel".
       */
      search.setType("video");
      /*
       * This method reduces the info returned to only the fields we need and makes calls more
       * efficient.
       */
      search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/publishedAt,snippet/thumbnails/default/url),pageInfo,prevPageToken,nextPageToken");
      search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
      
      //my filters
      search.setRelevanceLanguage("en");
      search.setRegionCode("US");
      search.setSafeSearch("strict");
      
      search.setPublishedAfter(publishedAfter);
      
      if(pageToken != null)
    	  search.setPageToken(pageToken);
      
      //adding duration of video
      //valid values of duration are short, medium, long, any
      search.setVideoDuration(duration);
      
      SearchListResponse searchResponse = search.execute();

      List<SearchResult> searchResultList = searchResponse.getItems();
      List<String> videoIds = new ArrayList<String>();

      searchResponseModel.setNextPageToken(searchResponse.getNextPageToken());
      searchResponseModel.setPrevPageToken(searchResponse.getPrevPageToken());
      
      if (searchResultList != null) {
    	  //get contentDetails.duration of video object for each item
    	  // Merge video IDs
          for (SearchResult searchResult : searchResultList) {
              videoIds.add(searchResult.getId().getVideoId());
          }
          Joiner stringJoiner = Joiner.on(',');
          String videoId = stringJoiner.join(videoIds);

          // Call the YouTube Data API's youtube.videos.list method to
          // retrieve the resources that represent the specified videos.
          YouTube.Videos.List listVideosRequest = youtube.videos().list("id, snippet, contentDetails").setId(videoId);
          listVideosRequest.setKey(apiKey);
          VideoListResponse listResponse = listVideosRequest.execute();

          List<Video> videoList = listResponse.getItems();
        videoModelList = prettyPrint(videoList.iterator(), query);
      }
      searchResponseModel.setSearchVideoModelList(videoModelList);
    } catch (GoogleJsonResponseException e) {
      System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
          + e.getDetails().getMessage());
      throw e;
    } catch (IOException e) {
      System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
      throw e;
    } catch (Throwable t) {
      t.printStackTrace();
      throw t;
    }
	return searchResponseModel;
  }

  /*
   * Returns a query term (String) from user via the terminal.
   */
  /*private static String getInputQuery() throws IOException {

    String inputQuery = "";

    System.out.print("Please enter a search term: ");
    BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
    inputQuery = bReader.readLine();

    if (inputQuery.length() < 1) {
      // If nothing is entered, defaults to "YouTube Developers Live."
      inputQuery = "YouTube Developers Live";
    }
    return inputQuery;
  }*/

  /*
   * Prints out all SearchResults in the Iterator. Each printed line includes title, id, and
   * thumbnail.
   *
   * @param iteratorSearchResults Iterator of SearchResults to print
   *
   * @param query Search query (String)
   */
  private static List<SearchVideoModel> prettyPrint(Iterator<Video> iteratorSearchResults, String query) {

	  List<SearchVideoModel> videoModelList = new ArrayList<SearchVideoModel>();
    //System.out.println("\n=============================================================");
    /*System.out.println(
        "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");*/
    //System.out.println("=============================================================\n");

    if (!iteratorSearchResults.hasNext()) {
      System.out.println(" There aren't any results for your query.");
    }

    while (iteratorSearchResults.hasNext()) {

      Video singleVideo = iteratorSearchResults.next();
      
      // Double checks the kind is video.
      if (singleVideo.getKind().equals("youtube#video")) {
        Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

        //System.out.println(" Video Id" + singleVideo.getId());
        SearchVideoModel vm = new SearchVideoModel();
        vm.setYoutubeId(singleVideo.getId());
        //System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
        vm.setTitle(singleVideo.getSnippet().getTitle());
        //System.out.println(" Description: " + singleVideo.getSnippet().getDescription());
        if(singleVideo.getSnippet().getDescription().length()>150){
        	vm.setDescription(singleVideo.getSnippet().getDescription().substring(0,150).concat("..."));
        } else {
        	vm.setDescription(singleVideo.getSnippet().getDescription());
        }	
        Date date = new Date(singleVideo.getSnippet().getPublishedAt().getValue());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        //System.out.println(" Published At: " + sdf.format(date));
        vm.setPublishedAt(sdf.format(date));
        //System.out.println(" Thumbnail: " + thumbnail.getUrl());
        vm.setThumbnailUrl(thumbnail.getUrl());
        //System.out.println(" Duration: "+ singleVideo.getContentDetails().getDuration());
        //convert duration
        //System.out.println("\n-------------------------------------------------------------\n");
        
        //make sure the default values are set for remaining fields:
        vm.setAddedToBookmarks(false);
        vm.setBookmarkId(0);
        vm.setAddedToLibrary(false);
        vm.setSegmentIds(new ArrayList<Integer>()); //this is useful for adding segment ids when checking if the video exists in library
        
        videoModelList.add(vm);
      }
    }
    return videoModelList;
  }

  public SearchResponseModel getVideoById(String youtube_id) throws Exception, Throwable {
	    // Read the developer key from youtube.properties
	    Properties properties = new Properties();
	    try {
	      InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
	      properties.load(in);

	    } catch (IOException e) {
	      System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
	          + " : " + e.getMessage());
	      System.exit(1);
	    }
	    SearchResponseModel searchResponseModel = new SearchResponseModel();
	    List<SearchVideoModel> videoModelList = new ArrayList<SearchVideoModel>();
	    try {
	      /*
	       * The YouTube object is used to make all API requests. The last argument is required, but
	       * because we don't need anything initialized when the HttpRequest is initialized, we override
	       * the interface and provide a no-op function.
	       */
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
	        public void initialize(HttpRequest request) throws IOException {}
	      }).setApplicationName("youtube-cmdline-search-sample").build();

	      // Get query term from user.
	      //String queryTerm = getInputQuery();

	      YouTube.Videos.List listVideosRequest = youtube.videos().list("id,snippet,contentDetails").setId(youtube_id);
	      String apiKey = properties.getProperty("youtube.apikey");
	      listVideosRequest.setKey(apiKey);
	      VideoListResponse listResponse = listVideosRequest.execute();
	      List<Video> videoList = listResponse.getItems();
	      
          if (videoList != null) {
        	  videoModelList = prettyPrint(videoList.iterator(), "");
          }
          searchResponseModel.setSearchVideoModelList(videoModelList);
	    } catch (GoogleJsonResponseException e) {
	      System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
	          + e.getDetails().getMessage());
	      throw e;
	    } catch (IOException e) {
	      System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
	      throw e;
	    } catch (Throwable t) {
	      t.printStackTrace();
	      throw t;
	    }
		return searchResponseModel;
	  }
  
  public static void main(String[] args) throws Exception, Throwable {
	//new Search().getVideoById("WPvGqX-TXP0");
	  new Search().getVideos("java", null, null, null);
}
}
