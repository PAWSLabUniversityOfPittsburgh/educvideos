package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.pitt.sis.exp.educvideos.videoauthoring.managers.SettingsManager;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.DomainModel;

/**
 * Servlet implementation class SettingsServlet
 */
@WebServlet("/SettingsServlet")
public class SettingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SettingsServlet() {
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

	/**
	 * 
	 * @param request
	 * @param response
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(false);
			if(session == null) {
				ServletContext servletContext = getServletContext();
				response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
			} else {
				String username = (String) session.getAttribute("user");
				if(username!=null) {
					SettingsManager settingsManager = new SettingsManager();
					List<DomainModel> domainModels = settingsManager.getDomainModelList();
					
					ServletContext servletContext = getServletContext();
					request.setAttribute("domainModels", domainModels);
		            RequestDispatcher rd = servletContext.getRequestDispatcher("/settings.jsp");
		            rd.forward(request, response);
				} else {
					session.invalidate();
					ServletContext servletContext = getServletContext();
					response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
