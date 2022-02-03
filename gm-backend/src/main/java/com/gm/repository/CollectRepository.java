package com.gm.repository;

import com.gm.model.Collect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Repository
public interface CollectRepository extends JpaRepository<Collect, Integer>, JpaSpecificationExecutor<Collect> {

    /**
     * Get all user
     * @param uid
     * @return
     */
    List<Collect> findByUserId(int uid);

    /**
     * Get follow user
     * @param cid
     * @param uid
     * @return
     */
    Collect findFirstByCollectUserIdAndUserId(int cid, int uid);

    /**
     * Get unfollow user
     * @param cid
     * @param uid
     * @return
     */
    long deleteByCollectUserIdAndUserId(int cid, int uid);

    /**
     * Get collect content by user
     * @param cid
     * @param uid
     * @return
     */
    Collect findFirstByContentIdAndUserId(int cid, int uid);

    /**
     * Get collect content list
     * @param userId
     * @return
     */
    List<Collect> findByUserIdAndContentIdIsNotNull(int userId);

    /**
     * Get collect content
     * @param userId
     * @return
     */
    List<Collect> findByUserIdAndCollectUserIdIsNotNull(int userId);

    /**
     * Get like content
     * @param goodContentId
     * @param userId
     * @return
     */
    Collect findFirstByGoodContentIdAndUserId(int goodContentId, int userId);

}
