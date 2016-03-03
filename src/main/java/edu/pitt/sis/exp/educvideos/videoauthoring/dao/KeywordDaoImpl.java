package edu.pitt.sis.exp.educvideos.videoauthoring.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Keyword;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class KeywordDaoImpl {

	/**
	 * This method adds a new keyword in the database
	 * @param keyword
	 * @return
	 * @throws Exception 
	 */
	public Integer addKeyword(Keyword keyword) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			 
			transaction = session.beginTransaction();
			
			int keywordId = (Integer)session.save(keyword);
			
			transaction.commit();
			
			return keywordId;
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
	 * This method gets all keywords from the database based on the entity and entity id.
	 * @param entity
	 * @param entityId
	 * @return
	 * @throws HibernateException
	 * @throws Exception
	 */
	public List<Keyword> getKeywordsByEntity(String entity, Integer entityId) throws HibernateException,Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();

			transaction = session.beginTransaction();

			List<Keyword> keywordList = (List<Keyword>) session
					.createCriteria(Keyword.class)
					.add(Restrictions.eq("entity", entity))
					.add(Restrictions.eq("entityId", entityId))
					.list();
			
			transaction.commit();
			
			return keywordList;
		} catch (HibernateException ex) {
			if (transaction != null)
				transaction.rollback();
			ex.getStackTrace();
			throw ex;
		} catch (Exception ex) {
			if (transaction != null)
				transaction.rollback();
			ex.getStackTrace();
			throw ex;
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	/**
	 * This method removes the keyword from the database.
	 * @param keyword
	 * @throws Exception
	 */
	public void deleteKeyword(int keywordId) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {			
			session = HibernateUtil.getSessionFactory().openSession();

			transaction = session.beginTransaction();

			Keyword k = (Keyword) session.get(Keyword.class, keywordId);
			session.delete(k);
			
			transaction.commit();
		} catch (HibernateException ex) {
			if (transaction != null)
				transaction.rollback();
			ex.getStackTrace();
			throw ex;
		} catch (Exception ex) {
			if (transaction != null)
				transaction.rollback();
			ex.getStackTrace();
			throw ex;
		} finally {
			if (session != null)
				session.close();
		}
	}
}
