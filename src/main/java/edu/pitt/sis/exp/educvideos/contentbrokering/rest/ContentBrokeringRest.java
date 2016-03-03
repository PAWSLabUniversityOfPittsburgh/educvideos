package edu.pitt.sis.exp.educvideos.contentbrokering.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import edu.pitt.sis.exp.educvideos.contentbrokering.dao.DomainDao;
import edu.pitt.sis.exp.educvideos.contentbrokering.dao.LibraryDao;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentDetailResponse;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentItem;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentItemDetail;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentPackage;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentPackageResponse;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentResponse;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentType;
import edu.pitt.sis.exp.educvideos.contentbrokering.models.ContentTypesResponseModel;

@Path("/v1")
public class ContentBrokeringRest {

	/**
	 * Fetches available content types - for now only videos are returned as response.
	 * Response is hard-coded.
	 * @return
	 */
	@GET
	@Path("/content")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContentTypes() {
		try{
			List<ContentType> contentTypes = new ArrayList<ContentType>();
			ContentType contentType = new ContentType();
			contentType.setName("videos");
			contentType.setUrl("http://columbus.exp.sis.pitt.edu/educvideos/");
			contentType.setTitle("Video Collection");
			contentType.setDescription("This is a collection of educational videos.");
			contentTypes.add(contentType);
			ContentTypesResponseModel ctResponseModel = new ContentTypesResponseModel();
			ctResponseModel.setContentTypes(contentTypes);
			GenericEntity<ContentTypesResponseModel> entity = new GenericEntity<ContentTypesResponseModel>(ctResponseModel) {};
			return Response.status(200).entity(entity).build();
		}
		catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	/**
	 * Fetches content packages (domains) of a content type (for now content type is videos)
	 * @return
	 */
	@GET
	@Path("/content/videos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContentPackagesForVideos() {
		try{
			DomainDao domainDao = new DomainDao();
			List<ContentPackage> contentPackages = domainDao.getAllDomains();
			ContentPackageResponse contentPackageResponse = new ContentPackageResponse();
			contentPackageResponse.setContentPackages(contentPackages);
			GenericEntity<ContentPackageResponse> entity = new GenericEntity<ContentPackageResponse>(contentPackageResponse) {};
			return Response.status(200).entity(entity).build();
		}
		catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	/**
	 * This web service fetches all the content of domain specified by the domain name.
	 * For example: videos-python
	 * @param domain
	 * @return
	 */
	@GET
	@Path("content/videos/{domain}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllVideosByDomain(@PathParam("domain") String domain) {
		try{
			LibraryDao libraryDao = new LibraryDao();
			List<ContentItem> contentItems = libraryDao.getAllVideosByDomain(domain);
			ContentResponse contentResponse = new ContentResponse();
			contentResponse.setContent(contentItems);
			GenericEntity<ContentResponse> entity = new GenericEntity<ContentResponse>(contentResponse) {};
			return Response.status(200).entity(entity).build();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
	
	/**
	 * This web service fetches all the details of content requested.
	 * For example: videos-python/vd_video0001_1
	 * @param domain
	 * @param content
	 * @return
	 */
	@GET
	@Path("content/videos/{domain}/{content}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVideoByName(@PathParam("domain") String domain, @PathParam("content") String content) {
		try {
			LibraryDao libraryDao = new LibraryDao();
			ContentItemDetail contentItemDetail = libraryDao.getVideoByDomain(domain, content);
			ContentDetailResponse contentDetailResponse = new ContentDetailResponse();
			contentDetailResponse.setItem(contentItemDetail);
			GenericEntity<ContentDetailResponse> entity = new GenericEntity<ContentDetailResponse>(contentDetailResponse) {};
			return Response.status(200).entity(entity).build();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
}
