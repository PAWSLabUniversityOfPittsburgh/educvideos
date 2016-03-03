package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.BookmarkDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.SegmentDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Bookmark;
import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.BookmarkModel;

/**
 * Servlet implementation class BookmarksServlet
 */
@WebServlet("/BookmarksServlet")
public class BookmarksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookmarksServlet() {
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
		try{
			HttpSession session = request.getSession(false);
			if(session == null) {
				ServletContext servletContext = getServletContext();
				response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
			} else {
				String username = (String) session.getAttribute("user");
				if(username!=null) {
					BookmarkDaoImpl bookmarkDaoImpl = new BookmarkDaoImpl();
					SegmentDaoImpl segmentDaoImpl = new SegmentDaoImpl();
					List<Bookmark> bookmarks = bookmarkDaoImpl.getAllBookmarksByAuthor(username);
					List<BookmarkModel> bookmarkModels = new ArrayList<BookmarkModel>();
					for(Bookmark bookmark: bookmarks) {
						BookmarkModel bookmarkModel = new BookmarkModel();
						bookmarkModel.setId(bookmark.getId());
						bookmarkModel.setVideoTitle(bookmark.getVideo().getTitle());
						bookmarkModel.setYoutubeId(bookmark.getVideo().getYoutubeId());
						bookmarkModel.setQuery(bookmark.getQuery());
						bookmarkModel.setNotes(bookmark.getNotes());
						//for each bookmarked video check if it is added to library
						//if yes then retrieve all their segment Ids
						List<Segment> segments = segmentDaoImpl.getSegmentsByYoutubeId(bookmark.getVideo().getYoutubeId());
						if(segments!=null && segments.size()>0) {
							bookmarkModel.setAddedToLibrary(true);
							List<Integer> segmentIds = new ArrayList<Integer>();
							for(Segment segment: segments) {
								segmentIds.add(segment.getId());
							}
							bookmarkModel.setSegmentIds(segmentIds);
						} else {
							bookmarkModel.setAddedToLibrary(false);
							bookmarkModel.setSegmentIds(new ArrayList<Integer>());
						}
						bookmarkModels.add(bookmarkModel);
					}
					ServletContext servletContext = getServletContext();
					request.setAttribute("bookmarkModels", bookmarkModels);
		            RequestDispatcher rd = servletContext.getRequestDispatcher("/bookmarks.jsp");
		            rd.forward(request, response);
					
				} else {
					session.invalidate();
					ServletContext servletContext = getServletContext();
					response.sendRedirect(servletContext.getContextPath()+"/LoginServlet");
				}
			}
			
		} catch(Exception e) {
			
		}
	}

}
