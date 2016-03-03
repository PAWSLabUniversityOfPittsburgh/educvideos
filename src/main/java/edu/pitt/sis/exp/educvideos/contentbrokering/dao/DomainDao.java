package edu.pitt.sis.exp.educvideos.contentbrokering.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.pitt.sis.exp.educvideos.entities.Domain;

import edu.pitt.sis.exp.educvideos.utils.EntityEnum;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentPackage;

public class DomainDao {
	
	public List<ContentPackage> getAllDomains() throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			
			transaction = session.beginTransaction();
			
			List<Domain> domainList = (List<Domain>)session.createCriteria(Domain.class).list();
			
			List<ContentPackage> contentPackages = new ArrayList<ContentPackage>();
			for(Domain domain: domainList){
				ContentPackage contentPackage = new ContentPackage();
				contentPackage.setName(domain.getName());
				//TODO hard-coded - find a better way to dynamically assign a base URL
				contentPackage.setUrl("http://columbus.exp.sis.pitt.edu/educvideos/api/v1/content/videos/"+domain.getName());
				contentPackage.setTitle(domain.getTitle());
				contentPackage.setDescription(domain.getDescription());
				if(domain.getAuthor()!=null)
					contentPackage.setAuthor(domain.getAuthor().getUsername());
				contentPackage.setLicense(domain.getLicense());
				contentPackage.setVersion(domain.getVersion());
				if(domain.getCreated()!=null)
					contentPackage.setCreated(domain.getCreated().toString());
				if(domain.getModified()!=null)
					contentPackage.setModified(domain.getModified().toString());
				KeywordDao keywordDao = new KeywordDao();
				List<String> keywords = keywordDao.getKeywordsByEntity(EntityEnum.Domain.toString(), domain.getId()); //listing keywords for each domain
				contentPackage.setKeywords(keywords);
				contentPackages.add(contentPackage);
			}
			
			transaction.commit();
			
			return contentPackages;
		}
		catch(HibernateException ex) {
			if(transaction!=null)
				transaction.rollback();
			ex.getStackTrace();
			throw ex;
		}
		catch(Exception ex) {
			if(transaction!=null)
				transaction.rollback();
			ex.getStackTrace();
			throw ex;
		}
		finally {
			if(session!=null)
				session.close();
		}
	}
}
