package com.gm.query;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class CommentQuery extends BaseQuery {

    public LocalDateTime startAddTime;

    public LocalDateTime endAddTime;

    public String content;

    public Integer userId;

}

