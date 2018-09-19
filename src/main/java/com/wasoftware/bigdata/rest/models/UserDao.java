package com.wasoftware.bigdata.rest.models;

import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;

@Transactional
public interface UserDao extends CrudRepository<User, Long>{

    User findByUsername(String username);
}
