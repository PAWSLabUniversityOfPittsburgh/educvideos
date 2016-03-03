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

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.DomainDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.KeywordDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Domain;
import edu.pitt.sis.exp.educvideos.entities.Keyword;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.DomainModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.KeywordModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.utils.Constants;

/**
 * Servlet implementation class ViewDomainServlet
 */
@WebServlet("/ViewDomainServlet")
public class ViewDomainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewDomainServlet() {
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
					String domainIdParam = request.getParameter("domainId");
					Integer domainId = Integer.parseInt(domainIdParam);
					
					//get domain entity
					DomainDaoImpl domainDaoImpl = new DomainDaoImpl();
					Domain domain = domainDaoImpl.getDomainById(domainId);
					
					//Safer to convert entity to model and then parse into JSON
					DomainModel domainModel = new DomainModel();
					domainModel.setId(domain.getId());
					domainModel.setName(domain.getName());
					domainModel.setTitle(domain.getTitle());
					domainModel.setDescription(domain.getDescription());
					domainModel.setAuthor(domain.getAuthor().getUsername());
					domainModel.setLicense(domain.getLicense());
					domainModel.setVersion(domain.getVersion());
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
					domainModel.setCreated(sdf.format(domain.getCreated()));
					domainModel.setModified(sdf.format(domain.getModified()));
					domainModel.setKeywords(new ArrayList<KeywordModel>());
					
					//get keywords of domain
					KeywordDaoImpl keywordDaoImpl = new KeywordDaoImpl();
					List<Keyword> keywords = keywordDaoImpl.getKeywordsByEntity(Constants.Domain, domain.getId());
					for(Keyword k: keywords) {
						KeywordModel keywordModel = new KeywordModel();
						keywordModel.setId(k.getId());
						keywordModel.setKeyword(k.getKeyword());
						domainModel.getKeywords().add(keywordModel);
					}
					
					//convert domain to JSON
					String jsonResponse = new Gson().toJson(domainModel);
					
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
