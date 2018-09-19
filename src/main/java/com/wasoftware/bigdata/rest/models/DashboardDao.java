package com.wasoftware.bigdata.rest.models;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface DashboardDao extends CrudRepository<Dashboard, Long> {

    List<Dashboard> findByUserid(long userid);
    Dashboard findByUseridAndId(long userid, long id);
    Dashboard findByUseridAndIsdefault(long userid, boolean isdefault);
    List<Dashboard> findByIssystemdefault(boolean issystemdefault);
    void deleteByUseridAndId(long userid, long id);

    @Modifying
    @Query("Update Dashboard set isDefault = 0 where userid = (:userid)")
    public void updateDefaultByUserid(@Param("userid") long userid);

}



