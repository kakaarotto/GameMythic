package com.gm.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author pujie
 */
@Entity
@Table(name = "collect")
@Data
@ToString
public class Collect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "add_time")
    private LocalDateTime addTime = LocalDateTime.now();

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    @Column(name = "collect_user_id")
    private Integer collectUserId;

    @Column(name = "content_id")
    private Integer contentId;

    @Column(name = "good_content_id")
    private Integer goodContentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;//用户ID

}
