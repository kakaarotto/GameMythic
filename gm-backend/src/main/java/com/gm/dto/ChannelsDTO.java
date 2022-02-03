package com.gm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelsDTO {

    private Integer channelsId;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    private String name;

    private String logo;

    private Boolean subscription = false;
}
