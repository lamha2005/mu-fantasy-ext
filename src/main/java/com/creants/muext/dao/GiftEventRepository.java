package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.event.HeroGift;

/**
 * @author LamHM
 *
 */
@Repository
public interface GiftEventRepository extends MongoRepository<HeroGift, String> {
}
