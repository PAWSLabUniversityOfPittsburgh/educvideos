package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.SegmentDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Keyword;
import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.KeywordModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.SegmentModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.utils.Constants;

/**
 * Servlet implementation class ViewSegmentServlet
 */
@WebServlet("/ViewSegmentServlet")
public class ViewSegmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewSegmentServlet() {
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
					String segmentIdParam = request.getParameter("segmentId");
					Integer segmentId = Integer.parseInt(segmentIdParam);
					
					SegmentDaoImpl segmentDaoImpl = new SegmentDaoImpl();
					Segment segment = segmentDaoImpl.getSegmentById(segmentId);
					
					//Safer to convert entity to model and then parse into JSON
					SegmentModel segmentModel = new SegmentModel();
					segmentModel.setId(segment.getId());
					segmentModel.setName(segment.getName());
					segmentModel.setTitle(segment.getTitle());
					segmentModel.setVideoTitle(segment.getVideo().getTitle());
					segmentModel.setYoutubeId(segment.getVideo().getYoutubeId());
					segmentModel.setStartTime(segment.getStartTime());
					segmentModel.setEndTime(segment.getEndTime());
					segmentModel.setTopicId(segment.getTopic().getId());
					segmentModel.setDescription(segment.getDescription());
					segmentModel.setAuthor(segment.getAuthor().getUsername());
					segmentModel.setVersion(segment.getVersion());
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
					segmentModel.setCreated(sdf.format(segment.getCreated()));
					segmentModel.setModified(sdf.format(segment.getModified()));
					segmentModel.setUrl(segment.getUrl());
					segmentModel.setKeywords(new ArrayList<KeywordModel>());
					
					//get keywords of segment
					KeywordDaoImpl keywordDaoImpl = new KeywordDaoImpl();
					List<Keyword> keywords = keywordDaoImpl.getKeywordsByEntity(Constants.Segment, segment.getId());
					if(keywords!=null && keywords.size()>0) {
						for(Keyword k: keywords) {
							KeywordModel keywordModel = new KeywordModel();
							keywordModel.setId(k.getId());
							keywordModel.setKeyword(k.getKeyword());
							segmentModel.getKeywords().add(keywordModel);
						}
					}
					//convert domain to JSON
					String jsonResponse = new Gson().toJson(segmentModel);
					
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
