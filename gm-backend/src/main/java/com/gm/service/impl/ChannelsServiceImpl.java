package com.gm.service.impl;

import com.gm.model.Channels;
import com.gm.model.ChannelsChannelsCategory;
import com.gm.model.ChannelsHistory;
import com.gm.model.ChannelsSubscription;
import com.gm.repository.ChannelsRepository;
import com.gm.query.ChannelsQuery;
import com.gm.service.ChannelsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pujie
 */
@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ChannelsServiceImpl implements ChannelsService {

    private final ChannelsRepository channelsRepository;

    @Override
    public Page<Channels> search(final ChannelsQuery query, Pageable pageable) {
        return channelsRepository.findAll((Specification<Channels>) (root, criteriaQuery, criteriaBuilder) -> rule(root, criteriaQuery, criteriaBuilder, query), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }

    @Override
    public Predicate rule(Root<Channels> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, final ChannelsQuery query) {
        List<Predicate> predicates = new ArrayList<>();
        if (query.getStartAddTime() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("addTime").as(LocalDateTime.class), query.getStartAddTime()));
        }
        if (query.getEndAddTime() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("addTime").as(LocalDateTime.class), query.getEndAddTime()));
        }
        if (query.getStartUpdateTime() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updateTime").as(LocalDateTime.class), query.getStartUpdateTime()));
        }
        if (query.getEndUpdateTime() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updateTime").as(LocalDateTime.class), query.getEndUpdateTime()));
        }
        if (StringUtils.isNotBlank(query.getName())) {
            predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + query.getName() + "%"));
        }
        if (query.getChannelsCategoryId() != null && query.getChannelsCategoryId() > 0) {
            final Subquery<Integer> subQuery = criteriaQuery.subquery(Integer.class);
            final Root<ChannelsChannelsCategory> rootIdInSQL = subQuery.from(ChannelsChannelsCategory.class);
            subQuery.select(rootIdInSQL.get("channelsId"));
            subQuery.where(criteriaBuilder.equal(rootIdInSQL.get("gameCategoryId"), query.getChannelsCategoryId()));
            predicates.add(criteriaBuilder.in(root.get("id")).value(subQuery));
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
    }

    @Override
    public Page<Channels> getSubscriptionChannels(final Integer userId, Pageable pageable) {
        if (userId != null && userId > 0) {
            log.info("###{}", pageable);
            return channelsRepository.getSubscriptionChannels(userId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
        }
        return null;
    }

    @Override
    public Page<Channels> getHistoryChannels(final Integer userId, Pageable pageable) {
        if (userId != null && userId > 0) {
            return channelsRepository.getHistoryChannels(userId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
        }
        return null;
    }
}
