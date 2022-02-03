package com.gm.vm;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class CommentVM {
    public String content;

    public Boolean enabled;

    public Integer userId;

    // Content ID
    public Integer goodsId;

}

