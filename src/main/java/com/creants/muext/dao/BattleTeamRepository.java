package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.BattleTeam;

@Repository
public interface BattleTeamRepository extends MongoRepository<BattleTeam, String> {

}
