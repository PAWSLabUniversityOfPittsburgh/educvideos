package edu.pitt.sis.exp.educvideos.contentbrokering.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Video;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class VideoDao {

	public int addVideo(Video video) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			 
			transaction = session.beginTransaction();
			
			int videoId = (Integer)session.save(video);
			
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
	
	public Video findByYoutubeId(String youtubeId) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
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
}
