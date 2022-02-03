package com.gm.query;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class MessageQuery  extends BaseQuery {

    public LocalDateTime startAddTime;

    public LocalDateTime endAddTime;

    private Integer type;

    private String title;

    private String content;

    private Integer userId;

    private Integer readState;
}
