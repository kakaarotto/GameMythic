package com.gm.query;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class ContentQuery extends BaseQuery {

    public LocalDateTime startAddTime;

    public LocalDateTime endAddTime;

    public LocalDateTime startUpdateTime;

    public LocalDateTime endUpdateTime;

    private String title;

    private String content;

    private Integer status;

    private Boolean collect = false;

    private List<Integer> userIds;

    private Integer userId;

    private Integer categoryId;

    private Integer channelsId;

    private Integer channelsCategoryId;

    private List<Integer> excludeIds;

    private Integer pageSize;
}

