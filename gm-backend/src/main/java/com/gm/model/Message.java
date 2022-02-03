package com.gm.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * @author pujie
 */
@Entity
@Table(name = "message")
@Data
@ToString
public class Message {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer type;

    @Column(name = "from_user_id")
    private Integer fromUserId;

    @Column(name = "from_type_id")
    private Integer fromTypeId;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "add_time")
    private LocalDateTime addTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "read_state")
    private Integer readState;

    @Column(name = "read_time")
    private LocalDateTime readTime;
}
