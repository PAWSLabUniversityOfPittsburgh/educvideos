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

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.KeywordDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.TopicDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Keyword;
import edu.pitt.sis.exp.educvideos.entities.Topic;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.KeywordModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.TopicModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.utils.Constants;

/**
 * Servlet implementation class ViewTopicServlet
 */
@WebServlet("/ViewTopicServlet")
public class ViewTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewTopicServlet() {
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
					String topicIdParam = request.getParameter("topicId");
					Integer topicId = Integer.parseInt(topicIdParam);
					
					TopicDaoImpl topicDaoImpl = new TopicDaoImpl();
					Topic topic = topicDaoImpl.getTopicById(topicId);
					
					//Safer to convert entity to model and then parse into JSON
					TopicModel topicModel = new TopicModel();
					topicModel.setId(topic.getId());
					topicModel.setTitle(topic.getTitle());
					topicModel.setDescription(topic.getDescription());
					topicModel.setDomainId(topic.getDomain().getId());
					topicModel.setKeywords(new ArrayList<KeywordModel>());
					
					//get keywords of topic
					KeywordDaoImpl keywordDaoImpl = new KeywordDaoImpl();
					List<Keyword> keywords = keywordDaoImpl.getKeywordsByEntity(Constants.Topic, topic.getId());
					for(Keyword k: keywords) {
						KeywordModel keywordModel = new KeywordModel();
						keywordModel.setId(k.getId());
						keywordModel.setKeyword(k.getKeyword());
						topicModel.getKeywords().add(keywordModel);
					}
					
					//convert domain to JSON
					String jsonResponse = new Gson().toJson(topicModel);
					
					out.write(jsonResponse);
				} else {
					session.invalidate();
					ServletContext servletContext = getServletContext();
					response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
