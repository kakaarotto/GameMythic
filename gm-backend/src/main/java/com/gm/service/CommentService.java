package com.gm.service;


import com.gm.model.Comment;
import com.gm.model.Content;
import com.gm.model.User;
import com.gm.query.CommentQuery;
import com.gm.vm.CommentVM;
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
public interface CommentService {
    /**
     * Create comment by user
     *
     * @param vm
     * @param user
     * @return
     */
    Comment create(CommentVM vm, User user);
    /**
     * delete comment
     * @param object
     * @return
     */
    Comment delete(Comment object);
}

