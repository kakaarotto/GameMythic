package com.gm.service.impl;

import com.gm.model.Content;
import com.gm.model.Message;
import com.gm.model.User;
import com.gm.query.MessageQuery;
import com.gm.repository.MessageRepository;
import com.gm.repository.UserRepository;
import com.gm.service.MessageService;
import com.gm.service.mapper.ContentMapper;
import com.gm.service.mapper.MessageMapper;
import com.gm.vm.ContentVM;
import com.gm.vm.MessageVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;

    public Predicate rule(Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, final MessageQuery query) {
        List<Predicate> predicates = new ArrayList<>();
        if (query.getStartAddTime() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("addTime").as(LocalDateTime.class), query.getStartAddTime()));
        }
        if (query.getEndAddTime() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("addTime").as(LocalDateTime.class), query.getEndAddTime()));
        }
        if (query.getReadState() != null) {
            predicates.add(criteriaBuilder.equal(root.get("readState").as(Integer.class), query.getReadState()));
        }
        if (query.getType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("type").as(Integer.class), query.getType()));
        }
        if (query.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("user").get("id").as(Integer.class), query.getUserId()));
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
    }


    @Override
    public Page<Message> search(MessageQuery query, Pageable pageable) {
        return messageRepository.findAll((Specification<Message>) (root, criteriaQuery, criteriaBuilder) -> rule(root, criteriaQuery, criteriaBuilder, query), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }

    @Override
    public Message getById(Integer messageId) {
        return messageRepository.getById(messageId);
    }

    @Override
    public Message create(MessageVM messageVM, User user) {
        Message object = new Message();
        MessageMapper.INSTANCE.vmToModel(messageVM, object);
        object.setUser(user);
        log.debug("Created Information for Message: {}", object);
        return messageRepository.save(object);
    }

    @Override
    public void delete(Integer messageId) {
        messageRepository.deleteById(messageId);
    }


    @Override
    public Message updateReadState(Integer messageId, Integer readState) {
        Message object = new Message();
        object.setId(messageId);
        object.setReadTime(LocalDateTime.now());
        object.setReadState(readState);
        return messageRepository.save(object);
    }
}
