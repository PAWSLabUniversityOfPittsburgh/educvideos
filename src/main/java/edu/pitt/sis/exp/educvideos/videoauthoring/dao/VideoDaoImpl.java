package edu.pitt.sis.exp.educvideos.videoauthoring.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Video;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class VideoDaoImpl {

	/**
	 * This method adds a video into database.
	 * @param video
	 * @return videoId (int)
	 * @throws Exception
	 */
	public int addVideo(Video video) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			int videoId = (Integer) session.save(video);
			
			transaction.commit();
			return videoId;
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
	
	//TODO: do you really need this video? you can delete a video by cascade delete - but is it okay?
	public void deleteVideo(Video video) throws Exception {
		
	}
	
	/**
	 * This method retrieves a video by its id.
	 * @param videoId
	 * @return Video
	 * @throws Exception
	 */
	public Video getVideoById(int id) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Video video = (Video) session.createCriteria(Video.class).add(Restrictions.eq("id", id)).uniqueResult();
			
			transaction.commit();
			
			return video;
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
	
	/**
	 * This method retrieves a video by its unique youtube id
	 * @param youtubeId
	 * @return Video
	 * @throws Exception
	 */
	public Video getVideoByYoutubeId(String youtubeId) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Video video = (Video) session.createCriteria(Video.class).add(Restrictions.eq("youtubeId", youtubeId)).uniqueResult();
			
			transaction.commit();
			
			return video;
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
	
	/**
	 * This method retrieves a list of all videos in database.
	 * @return List of Videos
	 * @throws Exception
	 */
	public List<Video> getAllVideos() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<Video> videos = session.createCriteria(Video.class).list();
			
			transaction.commit();
			
			return videos;
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
}
