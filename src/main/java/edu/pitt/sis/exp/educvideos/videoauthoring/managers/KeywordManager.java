package edu.pitt.sis.exp.educvideos.videoauthoring.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.exp.educvideos.videoauthoring.dao.KeywordDaoImpl;
import edu.pitt.sis.exp.educvideos.entities.Keyword;
import edu.pitt.sis.exp.educvideos.videoauthoring.models.KeywordModel;

public class KeywordManager {

	/**
	 * This method saves the updated list of keywords in the table.
	 * If the keyword does not exist in the keywordModels and it exists in the table then it is deleted.
	 * If the keyword does not exist in the table and it exists in the keywordModels then it is inserted.
	 * There is not update/edit operation on keyword.
	 * @param entity
	 * @param entityId
	 * @param keywordModels
	 */
	public void updateKeywords(String entity, Integer entityId, List<KeywordModel> keywordModels) {
		try {
			KeywordDaoImpl keywordDaoImpl = new KeywordDaoImpl();
			Map<Integer, String> keywordMap = new HashMap<Integer, String>();

			for(KeywordModel keywordModel: keywordModels) {
				//if id = 0 means save
				if(keywordModel.getId() == 0) {
					//saving new keyword
					Keyword keyword = new Keyword();
					keyword.setEntity(entity);
					keyword.setEntityId(entityId);
					keyword.setKeyword(keywordModel.getKeyword());
					int id = keywordDaoImpl.addKeyword(keyword);
					keywordMap.put(id,keywordModel.getKeyword());
				} else {;
					keywordMap.put(keywordModel.getId(), keywordModel.getKeyword());
				}
			}
			//first get all keywords
			List<Keyword> keywords = keywordDaoImpl.getKeywordsByEntity(entity, entityId);
			//if id != 0  means the keyword exists : then check if keyword model does not exist means remove the keyword
			for (Keyword keyword: keywords) {
				if(! keywordMap.containsValue(keyword.getKeyword())) {
					keywordDaoImpl.deleteKeyword(keyword.getId());
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
