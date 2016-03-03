package edu.pitt.sis.exp.educvideos.videoauthoring.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Domain;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class DomainDaoImpl {

	/**
	 * This method adds a new Domain into the database.
	 * @param domain
	 * @return domainId (int)
	 * @throws Exception
	 */
	public int addDomain(Domain domain) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			int domainId = (Integer) session.save(domain);
			
			transaction.commit();
			return domainId;
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
	 * This method updates Domain data in database.
	 * Editable fields are: title, description, license, version, modified.
	 * @param domain
	 * @throws Exception
	 */
	public void updateDomain(Domain domain) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(Domain.class).add(Restrictions.eq("id", domain.getId()));
			Domain d = (Domain) cr.uniqueResult();
			
			d.setTitle(domain.getTitle());
			d.setDescription(domain.getDescription());
			d.setLicense(domain.getLicense());
			d.setVersion(domain.getVersion());
			d.setModified(Calendar.getInstance().getTime());
			
			session.saveOrUpdate(d);
			
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
	 * This method deletes a domain based on its id.
	 * @param domain
	 * @throws Exception
	 */
	public void deleteDomain(Domain domain) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(Domain.class).add(Restrictions.eq("id", domain.getId()));
			Domain d = (Domain) cr.uniqueResult();
		
			session.delete(d); //TODO do you want to delete or do you want to deactivate the domain
			//TODO Resolve implications on User Activity and Segments and Topics and Keywords
			
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
	 * This method retrieves a domain by its id
	 * @param id
	 * @return Domain
	 * @throws Exception
	 */
	public Domain getDomainById(int id) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Domain domain = (Domain) session.createCriteria(Domain.class).add(Restrictions.eq("id", id)).uniqueResult();
			
			transaction.commit();
			
			return domain;
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
	 * This method retrieves a list of all domains in database
	 * @return
	 * @throws Exception
	 */
	public List<Domain> getAllDomains() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<Domain> domains = session.createCriteria(Domain.class).list();
			
			transaction.commit();
			
			return domains;
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
