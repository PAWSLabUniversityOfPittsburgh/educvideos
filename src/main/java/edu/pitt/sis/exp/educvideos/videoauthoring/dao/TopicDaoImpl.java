package edu.pitt.sis.exp.educvideos.videoauthoring.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Topic;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class TopicDaoImpl {

	/**
	 * This method adds a new Topic to database.
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public int addTopic(Topic topic) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			int topicId = (Integer) session.save(topic);
			
			transaction.commit();
			return topicId;
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
	 * This method updates the Topic data in database.
	 * Editable fields are: title, domain and description
	 * @param topic
	 * @throws Exception
	 */
	public void updateTopic(Topic topic) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(Topic.class).add(Restrictions.eq("id", topic.getId()));
			Topic t = (Topic) cr.uniqueResult();
			
			t.setTitle(topic.getTitle());
			//t.setDomain(topic.getDomain()); //I don't know how to manage the change of domain yet
			t.setDescription(topic.getDescription());
			
			session.saveOrUpdate(t);
			
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
	 * This method deletes the topic from database
	 * @param topic
	 * @throws Exception
	 */
	public void deleteTopic(Topic topic) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(Topic.class).add(Restrictions.eq("id", topic.getId()));
			Topic t = (Topic) cr.uniqueResult();
		
			session.delete(t); //TODO do you want to delete or do you want to deactivate the topic
			//TODO Resolve implications on User Activity and Segments and Keywords
			
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
	 * This method retrieves a topic by its id.
	 * @param id
	 * @return Topic
	 * @throws Exception
	 */
	public Topic getTopicById(int id) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Topic topic = (Topic) session.createCriteria(Topic.class).add(Restrictions.eq("id", id)).uniqueResult();
			
			transaction.commit();
			
			return topic;
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
	
	/** TODO this is not used anywhere currently
	 * This method retrieves topics by domain.
	 * @param domainId
	 * @return
	 * @throws Exception
	 */
	public List<Topic> getTopicsByDomain(int domainId) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<Topic> topics = session.createCriteria(Topic.class)
					.createAlias("domain", "d")
					.add(Restrictions.eq("d.id",domainId)).list();
			
			transaction.commit();
			
			return topics;
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
	 * This method retrieves all topics from the database.
	 * @return
	 * @throws Exception
	 */
	public List<Topic> getAllTopics() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<Topic> topics = session.createCriteria(Topic.class).list();
			
			transaction.commit();
			
			return topics;
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
