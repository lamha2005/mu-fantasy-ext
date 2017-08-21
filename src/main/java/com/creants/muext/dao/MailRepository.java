package com.creants.muext.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.Mail;

/**
 * @author LamHM
 *
 */
@Repository
public interface MailRepository extends MongoRepository<Mail, Long> {
	List<Mail> findByGameHeroIdAndReadIsFalse(String gameHeroId);


	List<Mail> findByGameHeroIdAndReadIsFalseAndReceivedIsFalse(String gameHeroId);


	List<Mail> findByGameHeroIdAndReceivedIsFalse(String gameHeroId);
}
