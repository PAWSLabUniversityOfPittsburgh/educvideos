package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.UserDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String username = request.getParameter("username");
	        String password = request.getParameter("password");
	                  
	        //validate the user
	        if(username != null && !username.isEmpty()){
	            UserDaoImpl userDaoImpl = new UserDaoImpl();
	            User user = userDaoImpl.getUserByUsername(username);
	            
	            ServletContext servletContext = getServletContext();
	            //check if user is present in the database
	            if(user!=null && user.getUsername() != null && password.equals(user.getPassword())){
	                HttpSession session = request.getSession(true);
	                session.setAttribute("user", user.getUsername());
					response.sendRedirect(servletContext.getContextPath()+"/SettingsServlet");
	            }
	            //if no username or invalid password then display error message of invalid credentials
	            else{
	                request.setAttribute("error", "Invalid username/password!");
	                RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/index.jsp");
	                requestDispatcher.forward(request,response);
	            }            
	        }
	        //This happens when 'logout' is clicked
	        else{
	            HttpSession session = request.getSession(false);
	            if(session!=null) {
	            	session.invalidate();
	            }
	            ServletContext servletContext = getServletContext();
	            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/index.jsp");
	            requestDispatcher.forward(request,response);
	        }
		} catch (ServletException e) {
			System.out.println("Exception in LoginServlet: "+e.getMessage());
		} catch (IOException e) {
			System.out.println("Exception in LoginServlet: "+e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception in LoginServlet: "+e.getMessage());
		}
	}

}
