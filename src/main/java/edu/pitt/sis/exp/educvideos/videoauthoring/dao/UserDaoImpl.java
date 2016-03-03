package edu.pitt.sis.exp.educvideos.videoauthoring.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.pitt.sis.exp.educvideos.entities.User;
import edu.pitt.sis.exp.educvideos.utils.HibernateUtil;

public class UserDaoImpl {

	/**
	 * This method adds a new user to the database.
	 * @param user
	 * @return userId (int)
	 * @throws Exception 
	 */
	public int addUser(User user) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			int userId = (Integer) session.save(user);
			
			transaction.commit();
			return userId;
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
	 * This method updates user information in database.
	 * Editable fields are: password and email.
	 * @param user
	 * @throws Exception 
	 */
	public void updateUser(User user) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(User.class).add(Restrictions.eq("username", user.getUsername()));
			User u = (User) cr.uniqueResult();
			
			u.setPassword(user.getPassword());
			u.setEmail(user.getEmail());
			
			session.saveOrUpdate(u);
			
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
	 * This method deletes the user from the database.
	 * TODO Do you really want to delete users, or do you want to deactivate them?
	 * @param user
	 * @throws Exception 
	 */
	public void deleteUser(User user) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			Criteria cr = session.createCriteria(User.class).add(Restrictions.eq("username", user.getUsername()));
			User u = (User) cr.uniqueResult();
			
			session.delete(u);
			
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
	 * This method retrieves the user by id.
	 * @param id
	 * @return User
	 * @throws Exception 
	 */
	public User getUserById(int id) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			User user = (User) session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
			
			transaction.commit();
			return user;
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
	 * This method retrieves user based on his username.
	 * @param username
	 * @return User
	 * @throws Exception 
	 */
	public User getUserByUsername(String username) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			User user = (User) session.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
			
			transaction.commit();
			return user;
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
	 * This method retrieves all users in database.
	 * @return List of users
	 * @throws Exception 
	 */
	public List<User> getAllUsers() throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			List<User> users = session.createCriteria(User.class).list();
			
			transaction.commit();
			
			return users;
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
