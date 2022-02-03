package com.gm.query;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class UserQuery extends BaseQuery {

    public Integer userId;

    public LocalDateTime startAddTime;

    public LocalDateTime endAddTime;

    public String username;

    public String userIds;

}
