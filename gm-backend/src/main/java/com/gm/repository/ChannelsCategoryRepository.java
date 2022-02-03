package com.gm.repository;

import com.gm.model.ChannelsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Repository
public interface ChannelsCategoryRepository extends JpaRepository<ChannelsCategory, Integer>, JpaSpecificationExecutor<ChannelsCategory> {

}
