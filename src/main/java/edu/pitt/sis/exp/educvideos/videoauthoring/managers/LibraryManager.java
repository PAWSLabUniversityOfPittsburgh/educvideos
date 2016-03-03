package edu.pitt.sis.exp.educvideos.videoauthoring.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.DomainDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.SegmentDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.TopicDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Domain;
import edu.pitt.sis.exp.educvideos.entities.Segment;
import edu.pitt.sis.exp.educvideos.entities.Topic;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.DomainModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.KeywordModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.SegmentModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.TopicModel;

public class LibraryManager {

	public List<DomainModel> getWholeLibrary() throws Exception {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			
			DomainDaoImpl domainDaoImpl = new DomainDaoImpl();
			TopicDaoImpl topicDaoImpl = new TopicDaoImpl();
			SegmentDaoImpl segmentDaoImpl = new SegmentDaoImpl();
			
			List<Domain> domains = domainDaoImpl.getAllDomains();
			List<Topic> topics = topicDaoImpl.getAllTopics();
			List<Segment> segments = segmentDaoImpl.getAllSegments();
			
			List<DomainModel> domainModels = new ArrayList<DomainModel>();
			
			Map<Integer, DomainModel> domainModelMap = new HashMap<Integer, DomainModel>();
			Map<Integer, TopicModel> topicModelMap = new HashMap<Integer, TopicModel>();
			
			for(Domain domain: domains) {
				DomainModel domainModel = new DomainModel();
				domainModel.setId(domain.getId());
				domainModel.setName(domain.getName());
				domainModel.setTitle(domain.getTitle());
				domainModel.setDescription(domain.getDescription());
				domainModel.setAuthor(domain.getAuthor().getUsername());
				domainModel.setLicense(domain.getLicense());
				domainModel.setVersion(domain.getVersion());
				domainModel.setCreated(sdf.format(domain.getCreated()));
				domainModel.setModified(sdf.format(domain.getModified()));
				domainModel.setTopics(new ArrayList<TopicModel>());
				domainModel.setKeywords(new ArrayList<KeywordModel>()); //TODO add keywords
				domainModelMap.put(domainModel.getId(),domainModel);
			}
			
			for(Topic topic: topics) {
				TopicModel topicModel = new TopicModel();
				topicModel.setId(topic.getId());
				topicModel.setTitle(topic.getTitle());
				topicModel.setDomainId(topic.getDomain().getId());
				topicModel.setDescription(topic.getDescription());
				topicModel.setKeywords(new ArrayList<KeywordModel>()); //TODO add keywords
				topicModel.setSegments(new ArrayList<SegmentModel>()); //In the next for loop we add all the segment models
				//no need to put in the map now, wait until segments are added
				topicModelMap.put(topicModel.getId(), topicModel);
			}
			
			for(Segment segment: segments) {
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
				segmentModel.setCreated(sdf.format(segment.getCreated()));
				segmentModel.setModified(sdf.format(segment.getModified()));
				segmentModel.setUrl(segment.getUrl());
				segmentModel.setKeywords(new ArrayList<KeywordModel>()); //TODO add keywords
				//retrieve the corresponding topic model in the map and put this segment model in the segment model list of topic model
				topicModelMap.get(segmentModel.getTopicId()).getSegments().add(segmentModel);
			}
			
			//put all the map entry values in the topicModels list
			for(Entry<Integer, TopicModel> mapEntry: topicModelMap.entrySet()) {
				TopicModel topicModel = mapEntry.getValue();
				//put this topicModel in corresponding topicModel of domainModelMap
				domainModelMap.get(topicModel.getDomainId()).getTopics().add(topicModel);
			}
			
			//put all the map entry values in the domainModels list
			for(Entry<Integer, DomainModel> mapEntry: domainModelMap.entrySet()) {
				domainModels.add(mapEntry.getValue());
			}
			
			return domainModels;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
