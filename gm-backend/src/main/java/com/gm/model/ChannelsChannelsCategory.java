package com.gm.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author pujie
 */
@Entity
@Table(name = "channels_game_categary")
@Data
@ToString
@IdClass(ChannelsChannelsCategoryKey.class)
public class ChannelsChannelsCategory implements Serializable {
    @Id
    @Column(name = "channels_id")
    private Integer channelsId;

    @Id
    @Column(name = "game_category_id")
    private Integer gameCategoryId;
}
