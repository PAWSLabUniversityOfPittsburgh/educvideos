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
 * Servlet implementation class SignupServlet
 */
@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignupServlet() {
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
		try{
			String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        String email = request.getParameter("email");
	        ServletContext servletContext = getServletContext();
	        
	        //check if username exists in database
	        UserDaoImpl userDaoImpl = new UserDaoImpl();
	        User user = userDaoImpl.getUserByUsername(username);
	        if(user != null && user.getUsername() != null){
	            request.setAttribute("errorUser", "Username already exists! Choose a different username.");
	            RequestDispatcher rd = servletContext.getRequestDispatcher("/index.jsp");
	            rd.forward(request, response);
	        } else{
	            User u = new User();
	            u.setUsername(username);
	            u.setPassword(password); 
	            u.setEmail(email);
	            int userId = userDaoImpl.addUser(u);
	            if(userId > 0){
	                HttpSession session = request.getSession(true);
	                session.setAttribute("user", u.getUsername());
	                RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/SettingsServlet");
	                requestDispatcher.forward(request,response);
	            }
	        }
		} catch(ServletException e) {
			System.out.println("Exception in SignupServlet: "+e.getStackTrace());
		} catch(IOException e) {
			System.out.println("Exception in SignupServlet: "+e.getStackTrace());
		} catch(Exception e) {
			System.out.println("Exception in SignupServlet: "+e.getStackTrace());
		}
	}
}
