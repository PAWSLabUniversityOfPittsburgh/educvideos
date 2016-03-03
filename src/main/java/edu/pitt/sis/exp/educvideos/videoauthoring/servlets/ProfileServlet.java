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
 * Servlet implementation class ProfileServlet
 */
@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(false);
			if(session == null) {
				ServletContext servletContext = getServletContext();
				response.sendRedirect(servletContext+"/LoginServlet");
			} else {
				String username = (String) session.getAttribute("user");
				if(username!=null) {
					UserDaoImpl userDaoImpl = new UserDaoImpl();
					User user = userDaoImpl.getUserByUsername(username);
					
					ServletContext servletContext = getServletContext();
					request.setAttribute("user", user);
		            RequestDispatcher rd = servletContext.getRequestDispatcher("/profile.jsp");
		            rd.forward(request, response);
				} else {
					session.invalidate();
					ServletContext servletContext = getServletContext();
					response.sendRedirect(servletContext+"/LoginServlet");
				}
			}
		} catch(Exception e) {
			System.out.println("Exception in ProfileServlet: "+e.getStackTrace());
		}
	}

}
