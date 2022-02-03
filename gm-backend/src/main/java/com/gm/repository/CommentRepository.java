package com.gm.repository;

import com.gm.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity
 * @author pujie
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    /**
     * find all comment
     * @param id
     * @return
     */
    List<Comment> findByGoodsId(int id);

}