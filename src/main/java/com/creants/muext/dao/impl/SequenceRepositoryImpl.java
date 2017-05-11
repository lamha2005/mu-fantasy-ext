package com.creants.muext.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.creants.muext.dao.SequenceRepository;
import com.creants.muext.entities.SequenceId;

/**
 * Trước khi deploy app lần đầu tiên chạy lệnh <br>
 * use mu-fantasy <br>
 * db.SequenceId.insert({"_id" : "quest_id", "_class" :
 * "com.creants.muext.entities.SequenceId", "seq" : 1})
 * 
 * @author LamHM
 *
 */
@Repository
public class SequenceRepositoryImpl implements SequenceRepository {
	@Autowired
	private MongoOperations mongoOperations;


	@Override
	public void createSequenceDocument(String documentName) {
		SequenceId sequence = new SequenceId();
		sequence.setId(documentName);
		sequence.setSeq(1);
		mongoOperations.save(sequence);
	}


	@Override
	public long getNextSequenceId(String key) {
		// get sequence id
		Query query = new Query(Criteria.where("_id").is(key));

		// increase sequence id by 1
		Update update = new Update().inc("seq", 1);

		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

		// this is the magic happened
		SequenceId sequenceId = mongoOperations.findAndModify(query, update, options, SequenceId.class);
		return sequenceId.getSeq();
	}

}
