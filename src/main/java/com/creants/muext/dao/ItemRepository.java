package com.creants.muext.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.creants.muext.entities.item.Item;

/**
 * @author LamHM
 */
@Repository
public interface ItemRepository extends MongoRepository<Item, Long> {

}
