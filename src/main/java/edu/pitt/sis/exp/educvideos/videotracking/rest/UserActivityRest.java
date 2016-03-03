package edu.pitt.sis.exp.educvideos.videotracking.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.exp.educvideos.entities.UserActivity;

import edu.pitt.sis.exp.educvideos.videotracking.dao.SegmentDao;
import edu.pitt.sis.exp.educvideos.videotracking.dao.UserActivityDao;

import edu.pitt.sis.exp.educvideos.videotracking.models.ContentModel;
import edu.pitt.sis.exp.educvideos.videotracking.models.SegmentModel;
import edu.pitt.sis.exp.educvideos.videotracking.models.UserActivityModel;
import edu.pitt.sis.exp.educvideos.videotracking.models.UserActivityResponseModel;
import edu.pitt.sis.exp.educvideos.videotracking.models.UserModel;
import edu.pitt.sis.exp.educvideos.videotracking.models.VideoModel;
import edu.pitt.sis.exp.educvideos.videotracking.models.VideoResponseModel;

@Path("/user")
public class UserActivityRest {

	@GET
	@Path("/helloworld")
	@Produces(MediaType.TEXT_PLAIN)
	public Response sayPlainTextHello() {
		System.out.println("in sayPlainTextHello");
	    return Response.status(200).entity("Hello Jersey").build();
	}
	
	@POST
	@Path("/video")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVideo(final VideoModel videoModel) {
		try{
			System.out.println("in getVideo");
			Integer sub = Integer.parseInt(videoModel.getSub());
			SegmentDao segmentDao = new SegmentDao();
			List<Object[]> videoList = segmentDao.getVideo(videoModel.getVideoid(), sub);
			List<SegmentModel> segmentModels = segmentDao.getSegments(videoModel.getVideoid());
			VideoResponseModel videoResponseModel = new VideoResponseModel();
			for(Object[] video: videoList) {
				videoResponseModel.setYoutubeId(video[0].toString());
				videoResponseModel.setStartTime(Integer.parseInt(video[1].toString()));
				videoResponseModel.setEndTime(Integer.parseInt(video[2].toString()));
				if(videoModel.getUsr()!=null && videoModel.getGrp()!=null && videoModel.getSid()!=null) {
					videoResponseModel.setValidUser(true);
				}
				else {
					videoResponseModel.setValidUser(false);
				}
				videoResponseModel.setSegments(segmentModels);
			}
			
			GenericEntity<VideoResponseModel> entity = new GenericEntity<VideoResponseModel>(videoResponseModel) {};
			return Response.status(200).entity(entity).build();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
	
	//save the user activity on the database
	@POST
	@Path("/saveActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveUserActivity(final UserModel userModel){
		try{
			System.out.println("in saveActivity"+userModel.getStart_time()+":"+userModel.getEnd_time()+" = "+userModel.getRes()+" and time spent: "+userModel.getTime_watched());
			UserActivityDao userActivityDao = new UserActivityDao();
			Integer id = userActivityDao.saveUserActivity(userModel);
			if(id==null)
				throw new Exception();	//TODO custom exception - when would there be such a situation
			GenericEntity<String> entity = new GenericEntity<String>("{\"success\":\"success\"}") {};
			return Response.status(200).entity(entity).build();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
	
	/**
	 * This webservice receives a user activity model from the aggregator
	 * and responds with the user activity for a requested set of videos
	 * @param userActivityModel
	 * @return
	 * @author Shruti Sabusuresh
	 */
	@POST
	@Path("/activity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserActivity(final UserActivityModel userActivityModel) {
		try {
			System.out.println("in getUserActivity");
			
			List<String> video_list = new ArrayList<String>();
			List<String> content_list = new ArrayList<String>();
			//tracking different providers using same service - like webex and animated examples
			for(String contentId: userActivityModel.getContent_list_by_provider().get(0).getContent_list()){
				//Split the contentId - vd_video001_1 - as video Id vd_video001 and segment Id 1
				//looks for the last occurrence of the underscore character
				String video_id = contentId.substring(0,contentId.lastIndexOf("_"));
				String segment_id = contentId.substring(contentId.lastIndexOf("_")+1);
				video_list.add(video_id);
				content_list.add(segment_id);
			}
			
			UserActivityDao userActivityDao = new UserActivityDao();
			List<UserActivity> userActivityList = userActivityDao.getAllUserActivity(userActivityModel.getUser_id(), video_list, content_list);
			Iterator<UserActivity> iter = userActivityList.iterator();
			List<ContentModel> contentList = new ArrayList<ContentModel>();
			while(iter.hasNext()){
				UserActivity uae = iter.next();
				ContentModel contentModel = new ContentModel();
				contentModel.setProgress(uae.getProgress());
				contentModel.setAnnotation_count(-1);
				contentModel.setAttempts(uae.getAttempts());
				contentModel.setContent_id(uae.getSegment().getVideo().getName()+"_"+uae.getSegment().getName());
				contentModel.setLike_count(-1);
				contentModel.setSub_activities(-1);
				contentModel.setSuccess_rate(-1);
				contentModel.setTime_spent(uae.getTimeSpent());
				contentList.add(contentModel);
			}
			UserActivityResponseModel userActivityResponseModel = new UserActivityResponseModel();
			userActivityResponseModel.setUser_id(userActivityModel.getUser_id());
			userActivityResponseModel.setGroup_id(userActivityModel.getGroup_id());
			userActivityResponseModel.setContent_list(contentList);
			
			GenericEntity<UserActivityResponseModel> entity = new GenericEntity<UserActivityResponseModel>(userActivityResponseModel) {};
			return Response.status(200).entity(entity).build();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
}
