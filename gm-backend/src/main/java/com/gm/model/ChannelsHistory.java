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
@Table(name = "channels_history")
@Data
@ToString
@IdClass(ChannelsHistoryKey.class)
public class ChannelsHistory implements Serializable {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "channels_id")
    private Integer channelsId;

    @Column(name = "add_time")
    private LocalDateTime addTime = LocalDateTime.now();
}

