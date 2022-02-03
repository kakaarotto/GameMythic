package com.gm.service;

import com.gm.model.ChannelsCategory;
import com.gm.query.ChannelsCategoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface ChannelsCategoryService {
    /**
     * Find All channel categories
     * @param query
     * @param pageable
     * @return
     */
    Page<ChannelsCategory> search(final ChannelsCategoryQuery query, Pageable pageable);

    /**
     * Build data query rules
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @param query
     * @return
     */
    Predicate rule(Root<ChannelsCategory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, final ChannelsCategoryQuery query);
}
