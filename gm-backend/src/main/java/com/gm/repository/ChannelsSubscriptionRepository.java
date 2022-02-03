package com.gm.repository;

import com.gm.model.Channels;
import com.gm.model.ChannelsSubscription;
import com.gm.model.ChannelsSubscriptionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelsSubscriptionRepository extends JpaRepository<ChannelsSubscription, ChannelsSubscriptionKey>, JpaSpecificationExecutor<Channels> {
    /**
     * Get the user subscription channel from the channel id list
     * @param userId
     * @param channelsIds
     * @return
     */
    List<ChannelsSubscription> findAllByUserIdAndChannelsIdIn(Integer userId, List<Integer> channelsIds);
}
