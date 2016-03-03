package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.BufferedReader;
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

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.DomainDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.TopicDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Topic;
import edu.pitt.sis.exp.educvideos.videoauthoring.managers.KeywordManager;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.TopicModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.utils.Constants;

/**
 * Servlet implementation class SaveTopicServlet
 */
@WebServlet("/SaveTopicServlet")
public class SaveTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveTopicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			HttpSession session = request.getSession(false);
			if(session == null) {
				ServletContext servletContext = getServletContext();
				response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
			} else {
				String username = (String) session.getAttribute("user");
				if(username!=null) {
					StringBuffer jb = new StringBuffer();
					String line = null;
					BufferedReader reader = request.getReader();
					while ((line = reader.readLine()) != null) {
						jb.append(line);
					}
					Gson gson = new Gson();
					TopicModel topicModel = gson.fromJson(jb.toString(),TopicModel.class);
					if(topicModel!=null) {
						DomainDaoImpl domainDaoImpl = new DomainDaoImpl();
						TopicDaoImpl topicDaoImpl = new TopicDaoImpl();
						
						Topic topic = new Topic();
						topic.setId(topicModel.getId());
						topic.setTitle(topicModel.getTitle());
						topic.setDescription(topicModel.getDescription());
						topic.setDomain(domainDaoImpl.getDomainById(topicModel.getDomainId()));
						
						if(topic.getId() == 0)
							topic.setId(topicDaoImpl.addTopic(topic));
						else
							topicDaoImpl.updateTopic(topic);
						
						//save keywords
						KeywordManager keywordManager = new KeywordManager();
						keywordManager.updateKeywords(Constants.Topic, topic.getId(), topicModel.getKeywords());
						
						out.write("success");
					} else {
						out.write("error");
					}
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
