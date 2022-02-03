package com.gm.service;

import com.gm.model.Category;
import com.gm.query.CategoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * @author pujie
 */
public interface CategoryService {


    /**
     * Find all categories
     * @param query
     * @param pageable
     * @return
     */
    Page<Category> search(final CategoryQuery query, Pageable pageable);

    /**
     * Build data query rules
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @param query
     * @return
     */
    Predicate rule(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder  , final CategoryQuery query);
}
