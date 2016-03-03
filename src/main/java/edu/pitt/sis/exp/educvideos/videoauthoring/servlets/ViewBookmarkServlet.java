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

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.BookmarkDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.KeywordDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.SegmentDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Bookmark;
import edu.pitt.sis.exp.educvideos.entities.Keyword;
import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.BookmarkModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.KeywordModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.utils.Constants;

/**
 * Servlet implementation class ViewBookmarkServlet
 */
@WebServlet("/ViewBookmarkServlet")
public class ViewBookmarkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewBookmarkServlet() {
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
					String bookmarkIdParam = request.getParameter("bookmarkId");
					Integer bookmarkId = Integer.parseInt(bookmarkIdParam);
					
					BookmarkDaoImpl bookmarkDaoImpl = new BookmarkDaoImpl();
					SegmentDaoImpl segmentDaoImpl = new SegmentDaoImpl();
					
					Bookmark bookmark = bookmarkDaoImpl.getBookmarkById(bookmarkId);
					
					//Safer to convert entity to model and then parse into JSON
					BookmarkModel bookmarkModel = new BookmarkModel();
					bookmarkModel.setId(bookmark.getId());
					bookmarkModel.setVideoTitle(bookmark.getVideo().getTitle());
					bookmarkModel.setYoutubeId(bookmark.getVideo().getYoutubeId());
					bookmarkModel.setQuery(bookmark.getQuery());
					bookmarkModel.setNotes(bookmark.getNotes());
					bookmarkModel.setAddedToLibrary(false);
					bookmarkModel.setSegmentIds(new ArrayList<Integer>());
					bookmarkModel.setKeywords(new ArrayList<KeywordModel>());
					
					//get all keywords of bookmark
					KeywordDaoImpl keywordDaoImpl = new KeywordDaoImpl();
					List<Keyword> keywords = keywordDaoImpl.getKeywordsByEntity(Constants.Bookmark, bookmark.getId());
					if(keywords!=null && keywords.size()>0) {
						for(Keyword k: keywords) {
							KeywordModel keywordModel = new KeywordModel();
							keywordModel.setId(k.getId());
							keywordModel.setKeyword(k.getKeyword());
							bookmarkModel.getKeywords().add(keywordModel);
						}
					}
					
					//get all segments for that bookmarked video
					List<Segment> segments = segmentDaoImpl.getSegmentsByYoutubeId(bookmark.getVideo().getYoutubeId());
					if(segments!=null && segments.size()>0) {
						bookmarkModel.setAddedToLibrary(true);
						for(Segment segment: segments) {
							bookmarkModel.getSegmentIds().add(segment.getId());
						}
					}
					
					//convert domain to JSON
					String jsonResponse = new Gson().toJson(bookmarkModel);
					
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
