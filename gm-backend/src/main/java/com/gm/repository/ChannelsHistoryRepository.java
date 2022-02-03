package com.gm.repository;

import com.gm.model.Channels;
import com.gm.model.ChannelsCategory;
import com.gm.model.ChannelsHistory;
import com.gm.model.ChannelsHistoryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelsHistoryRepository extends JpaRepository<ChannelsHistory, ChannelsHistoryKey>, JpaSpecificationExecutor<Channels> {
}
