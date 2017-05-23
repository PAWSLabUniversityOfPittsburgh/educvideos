package edu.pitt.sis.exp.educvideos.videotracking.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.entities.UserActivity;
import edu.pitt.sis.exp.educvideos.videotracking.models.UserModel;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;
import edu.pitt.sis.exp.educvideos.videotracking.utils.Interval;
import edu.pitt.sis.exp.educvideos.videotracking.utils.Solution;

public class UserActivityDao {

	public List<UserActivity> getAllUserActivity(String userId, List<String> video_list, List<String> content_list) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			
			session.beginTransaction();
			
			Criteria cr = session.createCriteria(UserActivity.class);
			cr.add(Restrictions.eq("userId", userId));
			cr.createAlias("segment", "s").add(Restrictions.in("s.name", content_list));
			cr.createAlias("s.video", "v");
			cr.add(Restrictions.in("v.name", video_list));
			
			List<UserActivity> userActivityList = cr.list();
			
			session.getTransaction().commit();
	
			return userActivityList;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			if(session!=null)
				session.close();
		}
	}
	
	public Integer saveUserActivity(UserModel userModel) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			Integer segmentName = Integer.parseInt(userModel.getSegment_id());
			
			session = HibernateUtil.getSessionFactory().openSession();			 
			transaction = session.beginTransaction();
			
			//get the segment
			Segment segment = (Segment) session.createCriteria(Segment.class).add(Restrictions.eq("name", userModel.getSegment_id()))
					.createAlias("video", "v").add(Restrictions.eq("v.name",userModel.getVideo_id())).uniqueResult();
			
			if(segment==null)
				throw new NullPointerException(); // use a custom Exception
			
			//find if the user-activity already exists - user+segment is unique
			UserActivity userActivity = (UserActivity) session.createCriteria(UserActivity.class)
					.add(Restrictions.eq("userId", userModel.getUser_id().toUpperCase())).add(Restrictions.eq("segment", segment)).uniqueResult();
			
			if(userActivity == null){
				System.out.println("Creating a new user activity in database");
				userActivity = new UserActivity();
				userActivity.setUserId(userModel.getUser_id().toUpperCase());
				userActivity.setGroupId(userModel.getGrp_id().toUpperCase());
				userActivity.setSegment(segment);
				userActivity.setAttempts(1);
				userActivity.setTimeSpent(userModel.getTime_watched());
				Integer user_start = userModel.getStart_time();
				Integer user_end = userModel.getEnd_time();
				userActivity.setStartToEndTime(user_start+"-"+user_end);
				userActivity.setProgress(userModel.getRes());
			}
			else{
				System.out.println("Updating user activity in database");
				//Attempts is the incremental
				userActivity.setAttempts(userActivity.getAttempts()+1);
				//Time spent is cumulative
				userActivity.setTimeSpent(userActivity.getTimeSpent()+userModel.getTime_watched());
				//check if the values are valid and no drag occurred
				Integer user_start = userModel.getStart_time();
				Integer user_end = userModel.getEnd_time();
				Integer video_start = userActivity.getSegment().getStartTime();
				Integer video_end = userActivity.getSegment().getEndTime();
				Integer video_length = video_end - video_start;
				//check if the userActivity table contains some range to merge
				if(userActivity.getStartToEndTime()!=null && userActivity.getStartToEndTime().length()>0) {
					System.out.println(":::");
					//add the new range in the database and merge the list
					String timeRangesString = userActivity.getStartToEndTime();
					System.out.println("--"+timeRangesString);
					String[]  timeRangesStrArray = timeRangesString.split(","); //csv values
					ArrayList<Interval> intervalArray = new ArrayList<Interval>(); //array input for merge-ranges algorithm
					for(String timeRangeStr: timeRangesStrArray) {
						String[] timeStr = timeRangeStr.split("-");
						Integer start_time = Integer.parseInt(timeStr[0]);
						Integer end_time = Integer.parseInt(timeStr[1]);
						Interval interval = new Interval(start_time,end_time);
						intervalArray.add(interval);
					}
					intervalArray.add(new Interval(user_start,user_end)); //add the current range
					//use the merge-ranges algorithm
					Solution solution = new Solution();
					ArrayList<Interval> result = solution.merge(intervalArray);
					//convert to csv format and also calculate the resultant progress
					Float total_time_watched = 0f;
					StringBuilder s = new StringBuilder();
					if(result.size()>0) {
						s.append(result.get(0).getStart()+"-"+result.get(0).getEnd());
						System.out.println("Adding: "+result.get(0).getStart()+"-"+result.get(0).getEnd());
						total_time_watched += (result.get(0).getEnd()-result.get(0).getStart());							
						for(int i=1; i<result.size(); i++) {
							s.append(","+result.get(i).getStart()+"-"+result.get(i).getEnd());
							System.out.println("Adding: "+result.get(i).getStart()+"-"+result.get(i).getEnd());
							total_time_watched += (result.get(i).getEnd()-result.get(i).getStart());
						}
					}
					userActivity.setStartToEndTime(s.toString());//save the result time ranges
					userActivity.setProgress(total_time_watched/video_length);
				}
				else {
					//save the current range as the first range in database
					System.out.println("new range: "+user_start+"-"+user_end);
					userActivity.setStartToEndTime(user_start+"-"+user_end);
					System.out.println("new res: "+userModel.getRes());
					userActivity.setProgress(userModel.getRes());
				}
			}
			
			session.saveOrUpdate(userActivity);
			transaction.commit();
			
			return userActivity.getId();
		}
		catch(HibernateException ex) {
			if(transaction!=null)
				transaction.rollback();
			System.err.println(ex.getStackTrace());
			throw ex;
		}
		catch(Exception ex) {
			if(transaction!=null)
				transaction.rollback();
			System.err.println(ex.getStackTrace());
			throw ex;
		}
		finally {
			if(session!=null)
				session.close();
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		UserActivityDao userActivityDao = new UserActivityDao();
		UserModel userModel = new UserModel();
		userModel.setUser_id("adl01");
		userModel.setGrp_id("ADL");
		userModel.setVideo_id("vd_video0001");
		userModel.setSegment_id("1");
		userModel.setRes(0.2f);
		userModel.setTime_watched(18);
		userModel.setStart_time(63);
		userModel.setEnd_time(81);
		System.out.println(userActivityDao.saveUserActivity(userModel));
		System.out.println("end");
	}
}
