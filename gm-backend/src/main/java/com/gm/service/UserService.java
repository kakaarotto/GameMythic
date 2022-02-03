package com.gm.service;

import com.gm.model.User;
import com.gm.query.UserQuery;
import com.gm.vm.UserVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.*;
import java.util.*;


/**
 * @author pujie
 */
public interface UserService {


    /**
     * Get all user
     * @param query
     * @param pageable
     * @return
     */
    Page<User> search(final UserQuery query, Pageable pageable);

    /**
     * Check User
     * @param query
     * @param pageable
     * @return
     */
    User check(final UserQuery query, Pageable pageable);

    /**
     *  Change password
     * @param user
     * @param password
     */
    void changePassword(User user, String password);

    /**
     * Get current user
     * @return
     */
    Optional<User> getCurrentUser();

    /**
     * Create user
     * @param vm
     * @return user
     */
    User create(UserVM vm);

    /**
     * Update user
     * @param object
     * @param vm
     * @return
     */
    User update(User object, UserVM vm);


    /**
     * Unfollow user
     * @param userId
     * @param user
     */
    void deleteByCollectUserIdAndUserId(int userId,User user);

    /**
     * Matches password
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    Boolean matches(CharSequence rawPassword, String encodedPassword);

    Predicate rule(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, final UserQuery query);
}