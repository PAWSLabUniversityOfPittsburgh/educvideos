package edu.pitt.sis.exp.educvideos.videoauthoring.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.Bookmark;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class BookmarkDaoImpl {

	/**
	 * This method adds a new bookmark to the database.
	 * @param bookmark
	 * @return bookmark id (int)
	 * @throws Exception
	 */
	public int addBookmark(Bookmark bookmark) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			int bookmarkId = (Integer) session.save(bookmark);
			
			transaction.commit();
			return bookmarkId;
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
	 * This method updates the bookmark in database
	 * Editable field is only notes.
	 * @param bookmark
	 * @throws Exception
	 */
	public void updateBookmark(Bookmark bookmark) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(Bookmark.class).add(Restrictions.eq("id", bookmark.getId()));
			Bookmark b = (Bookmark) cr.uniqueResult();
			
			b.setNotes(bookmark.getNotes());
			
			session.saveOrUpdate(b);
			
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
	 * This method removes the bookmark attached to the user
	 * @param bookmark
	 * @throws Exception
	 */
	public void deleteBookmark(Bookmark bookmark) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(Bookmark.class).add(Restrictions.eq("id", bookmark.getId()));
			Bookmark b = (Bookmark) cr.uniqueResult();
		
			session.delete(b); //TODO do you want to delete the video too?
			//TODO Resolve implications on Keywords and Videos
			
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
	 * This method retrieves the bookmark by its id
	 * @param id
	 * @return Bookmark
	 * @throws Exception
	 */
	public Bookmark getBookmarkById(int id) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Bookmark bookmark = (Bookmark) session.createCriteria(Bookmark.class).add(Restrictions.eq("id", id)).uniqueResult();
			
			transaction.commit();
			
			return bookmark;
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
	 * This method retrieves a list of bookmarks created by the user.
	 * @param username
	 * @return List of bookmarks
	 * @throws Exception
	 */
	public List<Bookmark> getAllBookmarksByAuthor(String username) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<Bookmark> bookmarks = session.createCriteria(Bookmark.class)
					.createAlias("author", "u")
					.add(Restrictions.eq("u.username", username)).list();
			
			transaction.commit();
			
			return bookmarks;
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
	 * This method retrieves bookmark by author and youtube id
	 * @param username
	 * @param youtubeid
	 * @return Bookmark
	 * @throws Exception
	 */
	public Bookmark getBookmarkByAuthorAndYoutubeId(String username, String youtubeid) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Bookmark bookmark = (Bookmark) session.createCriteria(Bookmark.class)
					.createAlias("author", "u")
					.add(Restrictions.eq("u.username", username))
					.createAlias("video", "v")
					.add(Restrictions.eq("v.youtubeId", youtubeid))
					.uniqueResult();
			
			transaction.commit();
			
			return bookmark;
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
