package edu.pitt.sis.exp.educvideos.videotracking.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.videotracking.models.SegmentModel;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class SegmentDao {

	public List<Object[]> getVideo(String videoName, Integer segmentName) throws Exception {
		try{
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			session.beginTransaction();
			
			String sql = "SELECT v.youtubeId, s.startTime, s.endTime FROM Segment s JOIN s.video v "
					+"WHERE v.name = '"+videoName+"' AND s.name = '"+segmentName.toString()+"'";
			Query query = session.createQuery(sql);
			List<Object[]> videoList = query.list();
			if(videoList==null || videoList.size()==0)
				throw new NullPointerException();	//TODO use custom exception
			return videoList;
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
	}
	
	public List<SegmentModel> getSegments(String videoName) throws Exception {
		try{
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			session.beginTransaction();
			
			List<Segment> segmentEntities = session.createCriteria(Segment.class).createAlias("video", "v").add(Restrictions.eq("v.name", videoName)).list();
			List<SegmentModel> segmentModels = new ArrayList<SegmentModel>();
			
			for(Segment segmentEntity: segmentEntities) {
				SegmentModel segmentModel = new SegmentModel();
				segmentModel.setId(Integer.parseInt(segmentEntity.getName()));
				segmentModel.setStart(segmentEntity.getStartTime());
				segmentModel.setEnd(segmentEntity.getEndTime());
				segmentModels.add(segmentModel);
			}
			
			return segmentModels;
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
	}
}
