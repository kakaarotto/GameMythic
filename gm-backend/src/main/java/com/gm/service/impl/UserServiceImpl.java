package com.gm.service.impl;

import com.gm.model.User;
import com.gm.repository.CollectRepository;
import com.gm.repository.UserRepository;
import com.gm.security.SecurityUtils;
import com.gm.service.UserService;
import com.gm.service.mapper.UserMapper;
import com.gm.query.UserQuery;
import com.gm.vm.UserVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CollectRepository collectRepository;


    @Override
    public Page<User> search(final UserQuery query, Pageable pageable) {
        return userRepository.findAll((Specification<User>) (root, criteriaQuery, criteriaBuilder) -> rule(root, criteriaQuery, criteriaBuilder, query), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }

    @Override
    public User check(final UserQuery query, Pageable pageable) {
        List<User> list = this.search(query, pageable.first()).getContent();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.debug("Changed password for User: {}", user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getCurrentUser() {
        return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneByEmail);
    }

    @Override
    public User create(UserVM vm) {
        User object = new User();
        UserMapper.INSTANCE.vmToModel(vm, object);
        // Encrypting user password
        String encryptedPassword = passwordEncoder.encode(vm.getPassword());
        object.setUsername(vm.getUsername());
        // Generate encrypted passwords
        object.setPassword(encryptedPassword);
        // default receive message
        object.setNotificationEnabled(true);
        object = userRepository.save(object);
        log.info("Created Information for User: {}", object);
        return object;
    }

    @Override
    public User update(User object, UserVM vm) {
        object.setUpdateTime(LocalDateTime.now());
        UserMapper.INSTANCE.vmToModel(vm, object);
        if (StringUtils.isNotEmpty(vm.getPassword())) {
            object.setPassword(passwordEncoder.encode(vm.getPassword()));
        }
        object = userRepository.save(object);
        return object;
    }


    @Override
    public void deleteByCollectUserIdAndUserId(int userId,User user){
        collectRepository.deleteByCollectUserIdAndUserId(userId, user.getId());
    }


    @Override
    public Boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public Predicate rule(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, final UserQuery query) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (query.getStartAddTime() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("addTime").as(LocalDateTime.class), query.getStartAddTime()));
        }
        if (query.getEndAddTime() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("addTime").as(LocalDateTime.class), query.getEndAddTime()));
        }
        if (query.getQ() != null && StringUtils.isNotBlank(query.getQ())) {
            predicates.add(
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("username").as(String.class), "%" + query.getQ() + "%")
                    )
            );
        }
        if (StringUtils.isNotBlank(query.getUsername())) {
            predicates.add(criteriaBuilder.like(root.get("username").as(String.class), "%" + query.getUsername() + "%"));
        }
        if (query.getEnabled() != null) {
            predicates.add(criteriaBuilder.equal(root.get("enabled").as(Boolean.class), query.getEnabled()));
        }
        if (query.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("id").as(Integer.class), query.getUserId()));
        }
        if (StringUtils.isNotBlank(query.getUserIds())) {
            CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get("id"));
            String[] userIds = StringUtils.split(query.getUserIds(), ",");
            for (int i = 0; i < userIds.length; i++) {
                in.value(Integer.valueOf(userIds[i]));
            }
            predicates.add(in);
        }
        return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
    }
}
