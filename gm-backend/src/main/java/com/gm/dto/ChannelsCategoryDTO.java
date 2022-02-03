package com.gm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelsCategoryDTO {

    private Integer channelsCategoryId;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    // Channels category name
    private String name;

}
