package com.gm.service.impl;

import com.gm.config.Constants;
import com.gm.model.Collect;
import com.gm.model.Content;
import com.gm.model.User;
import com.gm.repository.*;
import com.gm.service.ContentService;
import com.gm.service.MessageService;
import com.gm.service.mapper.ContentMapper;
import com.gm.query.ContentQuery;
import com.gm.vm.ContentVM;
import com.gm.vm.MessageVM;
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
public class ContentServiceImpl implements ContentService {
    
    private ContentRepository activityRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ChannelsRepository channelsRepository;
    private CollectRepository collectRepository;
    private MessageService messageService;

    @Override
    public List<Content> list(final ContentQuery query) {
        return this.list(query, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<Content> list(final ContentQuery query, Sort sort) {
        return activityRepository.findAll((Specification<Content>) (root, criteriaQuery, criteriaBuilder) -> rule(root, criteriaQuery, criteriaBuilder, query), sort);
    }

    @Override
    public Page<Content> search(final ContentQuery query, Pageable pageable) {
        return activityRepository.findAll((Specification<Content>) (root, criteriaQuery, criteriaBuilder) -> rule(root, criteriaQuery, criteriaBuilder, query), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }

    @Override
    public Content create(ContentVM contentVM, User user) {
        Content object = new Content();
        ContentMapper.INSTANCE.vmToModel(contentVM, object);
        object.setUser(user);
        object.setCategory(contentVM.getCategoryId() != null ? categoryRepository.getOne(contentVM.getCategoryId()) : null);
        object.setChannels(contentVM.getChannelsId() != null ? channelsRepository.getOne(contentVM.getChannelsId()) : null);
//        object.setChannelsCategory(contentVM.getChannelsCategoryId() != null ? channelsCategoryRepository.getOne(contentVM.getChannelsCategoryId()) : null);
        log.debug("Created Information for Activity: {}", object);
        return activityRepository.save(object);
    }

    @Override
    public Content update(Content object, ContentVM contentVM) {
        ContentMapper.INSTANCE.vmToModel(contentVM, object);
        object.setUpdateTime(LocalDateTime.now());
        log.debug("Changed Information for Activity: {}", object);
        return activityRepository.save(object);
    }

    @Override
    public void delete(Content object) {
        activityRepository.deleteById(object.getId());
    }

    @Override
    public Content collect(Content object, User user) {
        // Query collect records based on content id and user id
        Collect firstByContentIdAndUserId = collectRepository.findFirstByContentIdAndUserId(object.getId(), user.getId());

        if(firstByContentIdAndUserId != null){
            // Cancel collect
            collectRepository.delete(firstByContentIdAndUserId);
            object.setCollectCount(object.getCollectCount() - 1);
        }else {
            // Collect
            Collect collect = new Collect();
            collect.setUser(user);
            collect.setContentId(object.getId());
            collectRepository.save(collect);
            object.setCollectCount(object.getCollectCount() + 1);
        }
        return activityRepository.save(object);
    }


    @Override
    public Content goodContent(Content object, User loginUser) {
        // Query like records based on content id and user id
        Collect firstByContentIdAndUserId = collectRepository.findFirstByGoodContentIdAndUserId(object.getId(), loginUser.getId());
        if(firstByContentIdAndUserId != null){
            // Cancel like
            collectRepository.delete(firstByContentIdAndUserId);
            object.setGoodCount(object.getGoodCount() - 1);
        }else {
            // Like
            Collect good = new Collect();
            good.setUser(loginUser);
            good.setGoodContentId(object.getId());
            collectRepository.save(good);
            object.setGoodCount(object.getGoodCount() + 1);

            User contentUser = userRepository.findUserByUserId(object.getUser().getId());
            // Check if user message notification is available
            if(contentUser.getNotificationEnabled()) {
                // Create message
                MessageVM message = new MessageVM();
                message.setType(Constants.MESSAGE_liked);
                message.setFromUserId(loginUser.getId());
                message.setFromTypeId(object.getId());
                message.setTitle("Some one liked your content");
                message.setContent("Some one liked your content");
                message.setReadState(0);
                message.setAddTime(LocalDateTime.now());
                messageService.create(message, contentUser);
            }
        }
        return activityRepository.save(object);
    }

    @Override
    public Predicate rule(Root<Content> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, final ContentQuery query) {
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
        if (query.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status").as(Integer.class), query.getStatus()));
        }
        if (query.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("user").get("id").as(Integer.class), query.getUserId()));
        }
        if (query.getCategoryId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("category").get("id").as(Integer.class), query.getCategoryId()));
        }
        if (query.getChannelsId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("channels").get("id").as(Integer.class), query.getChannelsId()));
        }
        if (query.getUserIds() != null) {
            CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get("user").get("id"));
            for (int i = 0; i < query.getUserIds().size(); i++) {
                in.value(query.getUserIds().get(i));
            }
            predicates.add(in);
        }
        if (query.getEnabled() != null) {
            predicates.add(criteriaBuilder.equal(root.get("enabled").as(Boolean.class), query.getEnabled()));
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
    }

    @Override
    public Page<Content> searchByKeyword(String keyword, Pageable pageable) {
        // Dynamically generate query statements based on title
        Specification<Content> specification = (Specification<Content>) (root, query, criteriaBuilder) -> {
            List<Predicate> where = new ArrayList<>();
            // Check if the keyword is empty
            if (!StringUtils.isEmpty(keyword)) {
                where.add(criteriaBuilder.like(root.get("title"), "%" + keyword + "%" ));
            }
            return criteriaBuilder.and(where.toArray(new Predicate[0]));
        };
        return activityRepository.findAll(specification, pageable);
    }
}
