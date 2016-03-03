package edu.pitt.sis.exp.educvideos.videoauthoring.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.DomainDaoImpl;
import edu.pitt.sis.exp.educvideos.videoauthoring.dao.TopicDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Domain;
import edu.pitt.sis.exp.educvideos.entities.Topic;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.DomainModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.KeywordModel;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.TopicModel;

public class SettingsManager {

	public List<DomainModel> getDomainModelList() throws Exception {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			
			DomainDaoImpl domainDaoImpl = new DomainDaoImpl();
			TopicDaoImpl topicDaoImpl = new TopicDaoImpl();
			
			List<Domain> domains = domainDaoImpl.getAllDomains();
			List<Topic> topics = topicDaoImpl.getAllTopics();
			
			List<DomainModel> domainModels = new ArrayList<DomainModel>();
			Map<Integer,DomainModel> domainModelMap = new HashMap<Integer, DomainModel>();
			
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
				domainModelMap.get(topic.getDomain().getId()).getTopics().add(topicModel);
			}
			
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
