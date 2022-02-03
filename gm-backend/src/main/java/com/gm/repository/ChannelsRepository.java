package com.gm.repository;

import com.gm.model.Channels;
import com.gm.model.ChannelsCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Repository
public interface ChannelsRepository extends JpaRepository<Channels, Integer>, JpaSpecificationExecutor<Channels> {

    /**
     * Get the user current subscription channel
     * @param userId
     * @param pageable
     * @return
     */
    @Query(
            nativeQuery = true,
            value = "SELECT c.id AS id,c.name AS name,c.logo AS logo,c.add_time AS add_time,h.add_time AS update_time from channels_subscription h LEFT JOIN channels c ON h.channels_id=c.id WHERE h.user_id=?1",
            countQuery = "SELECT COUNT(*) from channels_subscription WHERE user_id=:userId"
    )
    Page<Channels> getSubscriptionChannels(Integer userId, Pageable pageable);

    /**
     * Get the user current history channel
     * @param userId
     * @param pageable
     * @return
     */
    @Query(
            nativeQuery = true,
            value = "SELECT c.id AS id,c.name AS name,c.logo AS logo,c.add_time AS add_time,h.add_time AS update_time from channels_history h LEFT JOIN channels c ON h.channels_id=c.id WHERE h.user_id=?1",
            countQuery = "SELECT COUNT(*) from channels_history WHERE user_id=:userId"
    )
    Page<Channels> getHistoryChannels(Integer userId, Pageable pageable);
}
