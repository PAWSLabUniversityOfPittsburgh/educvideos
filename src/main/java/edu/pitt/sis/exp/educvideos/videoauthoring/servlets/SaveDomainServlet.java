package edu.pitt.sis.exp.educvideos.videoauthoring.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.DomainDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.UserDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Domain;
import edu.pitt.sis.exp.educvideos.videoauthoring.managers.KeywordManager;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.DomainModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.utils.Constants;

/**
 * Servlet implementation class SaveDomainServlet
 */
@WebServlet("/SaveDomainServlet")
public class SaveDomainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveDomainServlet() {
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
					//Sun Oct 25 16:16:21 EDT 2015
					Gson gson = new Gson();
					DomainModel domainModel = gson.fromJson(jb.toString(),DomainModel.class);
					if(domainModel!=null) {
						
						UserDaoImpl userDaoImpl = new UserDaoImpl();
						DomainDaoImpl domainDaoImpl = new DomainDaoImpl();
						
						Domain domain = new Domain();
						domain.setId(domainModel.getId());
						domain.setName(domainModel.getName());
						domain.setTitle(domainModel.getTitle());
						domain.setDescription(domainModel.getDescription());
						domain.setAuthor(userDaoImpl.getUserByUsername(domainModel.getAuthor()));
						domain.setLicense(domainModel.getLicense());
						domain.setVersion(domainModel.getVersion());
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
						domain.setCreated(sdf.parse(domainModel.getCreated()));
						domain.setModified(sdf.parse(domainModel.getModified()));
						
						if(domain.getId() == 0)
							domain.setId(domainDaoImpl.addDomain(domain));
						else
							domainDaoImpl.updateDomain(domain);
						
						//save keywords
						KeywordManager keywordManager = new KeywordManager();
						keywordManager.updateKeywords(Constants.Domain, domain.getId(), domainModel.getKeywords());
						
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
			System.out.println("Exception in SaveDomainServlet: "+e.getStackTrace());
		}
	}

}
