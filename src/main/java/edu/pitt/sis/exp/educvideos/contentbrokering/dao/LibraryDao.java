package edu.pitt.sis.exp.educvideos.contentbrokering.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.utils.EntityEnum;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentItem;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentItemDetail;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.SubItem;

public class LibraryDao {
	
	public List<ContentItem> getAllVideosByDomain(String domain) throws HibernateException, Exception {
		Session session = null;
		Transaction transaction = null;
		try{			
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<Segment> segments = (List<Segment>) session.createCriteria(Segment.class).createAlias("topic", "t").createAlias("t.domain", "d")
					.add(Restrictions.eq("d.name", domain)).list();
			
			List<ContentItem> contentItems = new ArrayList<ContentItem>();
			for(Segment segment: segments) {
				ContentItem contentItem = new ContentItem();
				contentItem.setName(segment.getVideo().getName()+"_"+segment.getName());
				//TODO hard-coded - find a better way
				contentItem.setUrl("http://columbus.exp.sis.pitt.edu/educvideos/api/v1/content/videos/"+segment.getTopic().getDomain().getName()+"/"+contentItem.getName());
				contentItem.setHtmlUrl("http://columbus.exp.sis.pitt.edu/educvideos/loadVideo.html?videoid="+segment.getVideo().getName()+"&sub="+segment.getName());
				contentItem.setTitle(segment.getTitle());
				contentItem.setDescription(segment.getDescription());
				contentItem.setAuthor(segment.getAuthor().getUsername());
				contentItem.setVersion(segment.getVersion());
				if(segment.getCreated()!=null)
					contentItem.setCreated(segment.getCreated().toString());
				if(segment.getModified()!=null)
					contentItem.setModified(segment.getModified().toString());
				KeywordDao keywordDao = new KeywordDao();
				List<String> keywords = keywordDao.getKeywordsByEntity(EntityEnum.Segment.toString(), segment.getId()); //listing keywords for each segment
				contentItem.setKeywords(keywords);
				contentItems.add(contentItem);
			}
			transaction.commit();
			return contentItems;
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
	
	public ContentItemDetail getVideoByDomain(String domain, String content) throws HibernateException, Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			//TODO find a better way to parse the video id and segment id
			String video = content.substring(0, content.lastIndexOf("_"));
			String segmentId = content.substring(content.lastIndexOf("_")+1);
			
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Segment segment = (Segment) session.createCriteria(Segment.class)
					.add(Restrictions.eq("name", segmentId))
					.createAlias("topic", "t").createAlias("t.domain", "d")
					.add(Restrictions.eq("d.name", domain))
					.createAlias("video", "v")
					.add(Restrictions.eq("v.name", video))
					.uniqueResult();
			
			ContentItemDetail contentItemDetail = new ContentItemDetail();
			contentItemDetail.setName(segment.getVideo().getName()+"_"+segment.getName());
			//TODO hard-coded - find a better way
			contentItemDetail.setUrl("http://columbus.exp.sis.pitt.edu/educvideos/api/v1/content/videos/"+segment.getTopic().getDomain().getName()+"/"+contentItemDetail.getName());
			contentItemDetail.setHtmlUrl("http://columbus.exp.sis.pitt.edu/educvideos/loadVideo.html?videoid="+segment.getVideo().getName()+"&sub="+segment.getName());
			contentItemDetail.setTitle(segment.getTitle());
			contentItemDetail.setDescription(segment.getDescription());
			contentItemDetail.setAuthor(segment.getAuthor().getUsername());
			contentItemDetail.setVersion(segment.getVersion());
			if(segment.getCreated()!=null)
				contentItemDetail.setCreated(segment.getCreated().toString());
			if(segment.getModified()!=null)
				contentItemDetail.setModified(segment.getModified().toString());
			KeywordDao keywordDao = new KeywordDao();
			List<String> keywords = keywordDao.getKeywordsByEntity(EntityEnum.Segment.toString(), segment.getId()); //listing keywords for each segment
			contentItemDetail.setKeywords(keywords);
			SubItem subItem = new SubItem(); //TODO what would these be?
			subItem.setName("chapter1");
			subItem.setUrl("http://columbus.exp.sis.pitt.edu/educvideos/vd_video0001_1");
			List<String> subItemKeywords = new ArrayList<String>();
			subItemKeywords.add("basics");
			subItemKeywords.add("variables");
			subItemKeywords.add("assignments");
			subItem.setKeywords(subItemKeywords);
			List<SubItem> subItems = new ArrayList<SubItem>();
			subItems.add(subItem);
			contentItemDetail.setSubitems(subItems);
			
			transaction.commit();
			return contentItemDetail;
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
