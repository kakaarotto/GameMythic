package com.gm.service;

import com.gm.model.Channels;
import com.gm.query.ChannelsQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * @author pujie
 */
public interface ChannelsService {


    /**
     * Search channels
     * @param query
     * @param pageable
     * @return
     */
    Page<Channels> search(final ChannelsQuery query, Pageable pageable);

    /**
     * Build data query rules
     * @param root
     * @param cq
     * @param cb
     * @param query
     * @return
     */
    Predicate rule(Root<Channels> root, CriteriaQuery<?> cq, CriteriaBuilder cb, final ChannelsQuery query);

    /**
     * Get the user subscribed channels
     * @param userId
     * @param pageable
     * @return
     */
    Page<Channels> getSubscriptionChannels(final Integer userId, Pageable pageable);

    /**
     * Pagination get channel browsing history
     * @param userId
     * @param pageable
     * @return
     */
    Page<Channels> getHistoryChannels(final Integer userId, Pageable pageable);
}
