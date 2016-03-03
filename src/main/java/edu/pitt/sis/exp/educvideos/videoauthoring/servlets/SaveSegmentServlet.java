package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.SegmentDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.TopicDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.UserDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.VideoDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.entities.Video;
import edu.pitt.sis.exp.educvideos.videoauthoring.managers.KeywordManager;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.SegmentModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.utils.Constants;

/**
 * Servlet implementation class SaveSegmentServlet
 */
@WebServlet("/SaveSegmentServlet")
public class SaveSegmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveSegmentServlet() {
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
					StringBuffer jb = new StringBuffer();
					String line = null;
					BufferedReader reader = request.getReader();
					while ((line = reader.readLine()) != null) {
						jb.append(line);
					}
					
					Gson gson = new Gson();
					SegmentModel segmentModel = gson.fromJson(jb.toString(),SegmentModel.class);
					if(segmentModel!=null) {
						
						TopicDaoImpl topicDaoImpl = new TopicDaoImpl();
						UserDaoImpl userDaoImpl = new UserDaoImpl();
						VideoDaoImpl videoDaoImpl = new VideoDaoImpl();
						SegmentDaoImpl segmentDaoImpl = new SegmentDaoImpl();
						
						Segment segment = new Segment();
						segment.setId(segmentModel.getId());
						
						//setting the name - get all segments of the video
						//if none - set segmentName = 1
						//else set segmentName = segmentName of last segment in the array + 1
						List<Segment> segmentsOfVideo = segmentDaoImpl.getSegmentsByYoutubeId(segmentModel.getYoutubeId());
						Integer segmentName = 1;
						if(segmentsOfVideo != null && segmentsOfVideo.size()>0) {
							int lastIndex = segmentsOfVideo.size()-1;
							Segment lastSegment = segmentsOfVideo.get(lastIndex);
							Integer lastSegmentName = Integer.parseInt(lastSegment.getName());
							segmentName = lastSegmentName+1;
						}
						segment.setName(segmentName.toString());
						
						segment.setTitle(segmentModel.getTitle());
						Video video = videoDaoImpl.getVideoByYoutubeId(segmentModel.getYoutubeId());
						if(video == null) {
							//get the next video id available
							List<Video> videos = videoDaoImpl.getAllVideos();
							int videoId = videos.get(videos.size()-1).getId();
							videoId++;
							String videoIdStr = String.format("%04d", videoId);
							video = new Video();
							video.setName("vd_video"+videoIdStr);
							video.setTitle(segmentModel.getVideoTitle());
							video.setYoutubeId(segmentModel.getYoutubeId());
							video.setId(videoDaoImpl.addVideo(video));
						}
						segment.setVideo(video);
						segment.setStartTime(segmentModel.getStartTime());
						segment.setEndTime(segmentModel.getEndTime());
						segment.setTopic(topicDaoImpl.getTopicById(segmentModel.getTopicId()));
						segment.setDescription(segmentModel.getDescription());
						segment.setAuthor(userDaoImpl.getUserByUsername(segmentModel.getAuthor()));
						segment.setVersion(segmentModel.getVersion());
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
						segment.setCreated(sdf.parse(segmentModel.getCreated()));
						segment.setModified(Calendar.getInstance().getTime()); // set to current date
						segment.setUrl(""); //set the url somewhere
						
						if(segment.getId() == 0)
							segment.setId(segmentDaoImpl.addSegment(segment));
						else
							segmentDaoImpl.updateSegment(segment);
						
						//save keywords
						KeywordManager keywordManager = new KeywordManager();
						keywordManager.updateKeywords(Constants.Segment, segment.getId(), segmentModel.getKeywords());
						
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
