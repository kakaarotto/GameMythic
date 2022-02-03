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
@Data
@ToString
public class ChannelsCount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "add_time")
    private LocalDateTime addTime = LocalDateTime.now();

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    @Column
    private String name;

    @Column
    private String logo;

    @Column(name = "subscription_count")
    private Integer subscriptionCount;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "subscription")
    private Boolean subscription = false;
}
