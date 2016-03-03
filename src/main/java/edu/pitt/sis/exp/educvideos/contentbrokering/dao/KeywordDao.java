package edu.pitt.sis.exp.educvideos.contentbrokering.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Keyword;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class KeywordDao {

	public List<String> getKeywordsByEntity(String entity, Integer entityId) throws HibernateException,Exception {
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
			
			List<String> keywords = new ArrayList<String>();
			for(Keyword k: keywordList) {
				keywords.add(k.getKeyword());
			}
			
			transaction.commit();
			
			return keywords;
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
	
	public static void main(String[] args) throws HibernateException, Exception {
		(new KeywordDao()).getKeywordsByEntity("Domain",1);
	}
}
