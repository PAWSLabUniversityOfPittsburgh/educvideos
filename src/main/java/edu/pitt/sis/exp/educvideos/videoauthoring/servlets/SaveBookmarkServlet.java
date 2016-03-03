package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.BufferedReader;
import java.io.IOException;
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
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.UserDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.VideoDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Bookmark;
import edu.pitt.sis.exp.educvideos.entities.Video;
import edu.pitt.sis.exp.educvideos.videoauthoring.managers.KeywordManager;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.BookmarkModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.utils.Constants;

/**
 * Servlet implementation class SaveBookmarkServlet
 */
@WebServlet("/SaveBookmarkServlet")
public class SaveBookmarkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveBookmarkServlet() {
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
					StringBuffer jb = new StringBuffer();
					String line = null;
					BufferedReader reader = request.getReader();
					while ((line = reader.readLine()) != null) {
						jb.append(line);
					}
					
					Gson gson = new Gson();
					BookmarkModel bookmarkModel = gson.fromJson(jb.toString(),BookmarkModel.class);
					if(bookmarkModel!=null) {
						
						UserDaoImpl userDaoImpl = new UserDaoImpl();
						VideoDaoImpl videoDaoImpl = new VideoDaoImpl();
						BookmarkDaoImpl bookmarkDaoImpl = new BookmarkDaoImpl();
						
						Bookmark bookmark = new Bookmark();
						bookmark.setId(bookmarkModel.getId());
						bookmark.setNotes(bookmarkModel.getNotes());
						bookmark.setQuery(bookmarkModel.getQuery());
						bookmark.setAuthor(userDaoImpl.getUserByUsername(username));
						Video video = videoDaoImpl.getVideoByYoutubeId(bookmarkModel.getYoutubeId());
						if(video == null) {
							//get the next video id available
							List<Video> videos = videoDaoImpl.getAllVideos();
							int videoId = videos.get(videos.size()-1).getId();
							videoId++;
							String videoIdStr = String.format("%04d", videoId);
							video = new Video();
							video.setName("vd_video"+videoIdStr);
							video.setTitle(bookmarkModel.getVideoTitle());
							video.setYoutubeId(bookmarkModel.getYoutubeId());
							video.setId(videoDaoImpl.addVideo(video));
						}
						bookmark.setVideo(video);
						
						if(bookmark.getId() == 0)
							bookmarkDaoImpl.addBookmark(bookmark);
						else
							bookmarkDaoImpl.updateBookmark(bookmark);
						
						//save keywords
						KeywordManager keywordManager = new KeywordManager();
						keywordManager.updateKeywords(Constants.Bookmark, bookmark.getId(), bookmarkModel.getKeywords());
						
						response.getWriter().write("success");
					}
				} else {
					session.invalidate();
					ServletContext servletContext = getServletContext();
					response.sendRedirect(servletContext+"/LoginServlet");
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
