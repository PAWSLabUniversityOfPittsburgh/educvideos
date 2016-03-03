package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.DomainDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.TopicDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Domain;
import edu.pitt.sis.exp.educvideos.entities.Topic;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.DomainModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.TopicModel;

/**
 * Servlet implementation class ViewAllDomainsServlet
 */
@WebServlet("/ViewAllDomainsServlet")
public class ViewAllDomainsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewAllDomainsServlet() {
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
					DomainDaoImpl domainDaoImpl = new DomainDaoImpl();
					TopicDaoImpl topicDaoImpl = new TopicDaoImpl();
					
					//retrieve all domains
					List<Domain> domains = domainDaoImpl.getAllDomains();
					List<DomainModel> domainModels = new ArrayList<DomainModel>();
					if(domains != null) {
						for(Domain domain: domains) {
							DomainModel domainModel = new DomainModel();
							domainModel.setId(domain.getId());
							domainModel.setTitle(domain.getTitle());
							//retrieve all topic by domain
							List<Topic> topics = topicDaoImpl.getTopicsByDomain(domain.getId());
							List<TopicModel> topicModels = new ArrayList<TopicModel>();
							if(topics != null) {
								for(Topic topic: topics) {
									TopicModel topicModel = new TopicModel();
									topicModel.setId(topic.getId());
									topicModel.setTitle(topic.getTitle());
									topicModels.add(topicModel);
								}
							}
							domainModel.setTopics(topicModels);
							domainModels.add(domainModel);
						}
					}
					
					//convert to json
					String jsonResponse = new Gson().toJson(domainModels);
					
					out.write(jsonResponse);
				} else {
					session.invalidate();
					ServletContext servletContext = getServletContext();
					response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in ViewAllDomainsServlet: "+e.getStackTrace());
		}
	}

	
}
