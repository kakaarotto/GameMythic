package com.gm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gm.config.Constants;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pujie
 */
@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 0)
    private LocalDateTime addTime = LocalDateTime.now();

    @Column(nullable = false, length = 0)
    private LocalDateTime updateTime = LocalDateTime.now();

    @NotNull
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 16)
    private String password;

    @Column(length = 20)
    private String email;

    @Column(name = "avatar", length = 50)
    private String avatar = Constants.USER_DEFAULT_PHOTO;

    @Column(name = "account_enabled")
    private Boolean enabled = true;

    @Column(name = "notification_enabled")
    private Boolean notificationEnabled = false;

}