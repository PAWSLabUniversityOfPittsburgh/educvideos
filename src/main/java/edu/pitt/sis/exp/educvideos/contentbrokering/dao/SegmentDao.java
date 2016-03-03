package edu.pitt.sis.exp.educvideos.contentbrokering.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.entities.Video;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class SegmentDao {

	public Segment findByVideoAndTimes(Video video, Integer startTime, Integer endTime) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Segment segment = (Segment) session.createCriteria(Segment.class)
					.add(Restrictions.eq("video", video))
					.add(Restrictions.eq("startTime", startTime))
					.add(Restrictions.eq("endTime", endTime)).uniqueResult();
			
			transaction.commit();
			return segment;
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
	
	public Integer findByVideo(Video video) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Integer maxSegmentName = (Integer) session.createCriteria(Segment.class)
					.add(Restrictions.eq("video", video))
					.setProjection(Projections.max("segmentName")).uniqueResult();
			
			transaction.commit();
			return maxSegmentName;
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

	public int addSegment(Segment segment) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			 
			transaction = session.beginTransaction();
			
			int segmentId = (Integer)session.save(segment);
			
			transaction.commit();
			
			return segmentId;
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
