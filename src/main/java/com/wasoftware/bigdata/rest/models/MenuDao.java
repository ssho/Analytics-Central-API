package com.wasoftware.bigdata.rest.models;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface MenuDao extends CrudRepository<Menu, Long> {

    Menu findByGroupName(String groupName);
}
