package com.gm.query;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class CategoryQuery extends BaseQuery {


    public LocalDateTime startAddTime;

    public LocalDateTime endAddTime;

    public LocalDateTime startUpdateTime;

    public LocalDateTime endUpdateTime;

    private String name;

}

