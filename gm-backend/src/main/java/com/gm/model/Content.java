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
@Table(name = "content")
@Data
@ToString
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "add_time")
    private LocalDateTime addTime = LocalDateTime.now();

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String content;

    @Column
    private String images;

    @Column(name = "good_count")
    private Integer goodCount = 0;

    @Column(name = "collect_count")
    private Integer collectCount = 0;

    @Column
    private Integer status = 1;

    @Column
    private Boolean enabled = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channels_id")
    private Channels channels;

}
