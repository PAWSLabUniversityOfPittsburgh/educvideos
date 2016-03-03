package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import edu.pitt.sis.exp.educvideos.videoauthoring.managers.SearchManager;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.SearchResponseModel;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
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
			PrintWriter out = response.getWriter();
			HttpSession session = request.getSession(false);
			if(session == null) {
				ServletContext servletContext = getServletContext();
				response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
			} else {
				String username = (String) session.getAttribute("user");
				if(username!=null) {
					//check if youtube id or query is null
					String query = request.getParameter("query");
					String date = request.getParameter("date");
					String page = request.getParameter("page");
					String duration = request.getParameter("duration");
					String youtubeid = request.getParameter("youtubeid");
					
					SearchManager searchManager = new SearchManager();
					SearchResponseModel searchResponseModel = null;
					if(query!=null && date!=null && page!=null && duration!=null) {
						searchResponseModel = searchManager.getSearchResponse(query, date, page, duration, username);
					}
					else if(youtubeid!=null) {
						searchResponseModel = searchManager.getSearchResponseById(youtubeid, username);
					}
					
					String jsonResponse = new Gson().toJson(searchResponseModel);
					
					response.setContentType("application/json");
				    response.setCharacterEncoding("UTF-8");
					out.write(jsonResponse);
				} else {
					session.invalidate();
					ServletContext servletContext = getServletContext();
					response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
				}
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
