package com.gm.service.impl;

import com.gm.model.Comment;
import com.gm.model.Content;
import com.gm.model.User;
import com.gm.repository.CommentRepository;
import com.gm.repository.ContentRepository;
import com.gm.service.CommentService;
import com.gm.service.mapper.CommentMapper;
import com.gm.query.CommentQuery;
import com.gm.vm.CommentVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ContentRepository contentRepository;

    @Override
    public Comment create(CommentVM vm, User user) {
        Comment object = new Comment();
        CommentMapper.INSTANCE.vmToModel(vm, object);
        // Find the content according to the content id, if it exists, the comment data is recorded in the content id
        object.setGoods(vm.getGoodsId() != null ? contentRepository.getById(vm.getGoodsId()) : null);
        object.setUser(user);
        object = commentRepository.save(object);
        log.debug("Created Information for Comment: {}", object);
        return object;
    }

    @Override
    public Comment delete(Comment object) {
        object.setEnabled(false);
        return commentRepository.save(object);
    }
}
