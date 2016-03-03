package edu.pitt.sis.exp.educvideos.videoauthoring.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class SegmentDaoImpl {

	/**
	 * This method adds a new segment in database.
	 * @param segment
	 * @return
	 * @throws Exception
	 */
	public int addSegment(Segment segment) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			int segmentId = (Integer) session.save(segment);
			
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
	
	/**
	 * This method updates segment info in database.
	 * Editable fields: title, startTime, endTime, topic and domain, description, version and modified
	 * @param segment
	 * @throws Exception
	 */
	public void updateSegment(Segment segment) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(Segment.class).add(Restrictions.eq("id", segment.getId()));
			Segment s = (Segment) cr.uniqueResult();
			
			s.setTitle(segment.getTitle());
			s.setStartTime(segment.getStartTime());
			s.setEndTime(segment.getEndTime());
			s.setTopic(segment.getTopic());
			s.setDescription(segment.getDescription());
			s.setVersion(segment.getVersion());
			s.setModified(Calendar.getInstance().getTime());
			
			session.saveOrUpdate(s);
			
			transaction.commit();
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
	 * This method deletes a segment based on its id
	 * @param segment
	 * @throws Exception
	 */
	public void deleteSegment(Segment segment) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(Segment.class).add(Restrictions.eq("id", segment.getId()));
			Segment s = (Segment) cr.uniqueResult();
		
			session.delete(s); //TODO do you want to delete or do you want to deactivate the video?
			//TODO Resolve implications on User Activity and Keywords
			
			transaction.commit();
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
	 * This method retrieves a segment by its id.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Segment getSegmentById(int id) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Segment segment = (Segment) session.createCriteria(Segment.class).add(Restrictions.eq("id", id)).uniqueResult();
			
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
	
	/**
	 * This method retrieves all segments in database.
	 * @return
	 * @throws Exception
	 */
	public List<Segment> getAllSegments() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<Segment> segments = session.createCriteria(Segment.class).list();
			
			transaction.commit();
			
			return segments;
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
	 * This method retrieves list of segments by youtube id
	 * @param youtubeid
	 * @return List of Segment
	 * @throws Exception
	 */
	public List<Segment> getSegmentsByYoutubeId(String youtubeid) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<Segment> segments = session.createCriteria(Segment.class)
					.createAlias("video", "v").add(Restrictions.eq("v.youtubeId", youtubeid)).list();
			
			transaction.commit();
			
			return segments;
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
