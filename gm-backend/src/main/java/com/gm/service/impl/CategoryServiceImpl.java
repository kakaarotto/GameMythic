package com.gm.service.impl;

import com.gm.model.Category;
import com.gm.repository.CategoryRepository;
import com.gm.service.CategoryService;
import com.gm.query.CategoryQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<Category> search(CategoryQuery query, Pageable pageable) {
        return categoryRepository.findAll((Specification<Category>) (root, criteriaQuery, criteriaBuilder) -> rule(root, criteriaQuery, criteriaBuilder, query), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }

    @Override
    public Predicate rule(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, CategoryQuery query){
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
        return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
    }

}
