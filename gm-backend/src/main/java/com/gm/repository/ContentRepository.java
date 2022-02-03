package com.gm.repository;

import com.gm.model.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Integer>, JpaSpecificationExecutor<Content> {

    /**
     * Get collect id
     * @param ids
     * @return
     */
    List<Content> findByIdIn(List ids);


    /**
     * Get random channle
     * @param size
     * @param channelsId
     * @return
     */
    @Query(value = "select * from content where channels_id=:channelsId ORDER BY RAND() LIMIT :size", nativeQuery = true)
    List<Content> getRandChannelContents(@Param("size") Integer size, @Param("channelsId") Integer channelsId);
}
