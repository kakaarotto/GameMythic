package com.gm.repository;

import com.gm.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * Get follow other users
     * @param userId
     * @return
     */
    @Query(value = "select * from `user` where id = :userId", nativeQuery = true)
    User findUserByUserId(@Param("userId") Integer userId);


    /**
     * Get username for verify
     * @param username
     * @return
     */
    Optional<User> findOneByUsernameIgnoreCase(String username);

    /**
     * Get email for verify
     * @param email
     * @return
     */
    Optional<User> findOneByEmailIgnoreCase(String email);

    /**
     * Get username
     * @param username
     * @return
     */
    Optional<User> findOneByUsername(String username);

    /**
     * Get email
     * @param email
     * @return
     */
    @Cacheable(cacheNames = "users")
    Optional<User> findOneByEmail(String email);


}
