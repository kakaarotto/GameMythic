package com.gm.service;


import com.gm.model.Content;
import com.gm.model.User;
import com.gm.query.ContentQuery;
import com.gm.vm.ContentVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


/**
 * @author pujie
 */
public interface ContentService {


     /**
      * List content sort ASC
      * @param query
      * @return
      */
     List<Content> list(final ContentQuery query);

     /**
      * List all content
      * @param query
      * @param sort
      * @return
      */
     List<Content> list(final ContentQuery query, Sort sort);

     /**
      * Search content
      * @param query
      * @param pageable
      * @return
      */
     Page<Content> search(final ContentQuery query, Pageable pageable);


     /**
      * Creat content
      * @param contentVM
      * @param user
      * @return
      */
     Content create(ContentVM contentVM, User user);

     /**
      * Update content
      * @param object
      * @param contentVM
      * @return
      */
     Content update(Content object, ContentVM contentVM);

     /**
      * Delete content
      * @param object
      */
     void delete(Content object);

     /**
      * Collect content
      * @param object
      * @param user
      * @return
      */
     Content collect(Content object, User user);


     /**
      * Like content
      * @param object
      * @param user
      * @return
      */
     Content goodContent(Content object, User user);

     /**
      * Build data query rules
      * @param root
      * @param criteriaQuery
      * @param criteriaBuilder
      * @param query
      * @return
      */
     Predicate rule(Root<Content> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, final ContentQuery query);

     /**
      * Search content by keyword
      * @param keyword
      * @param pageable
      * @return
      */
     Page<Content> searchByKeyword(String keyword, Pageable pageable);
}
