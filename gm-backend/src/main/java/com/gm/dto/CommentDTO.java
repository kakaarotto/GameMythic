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
public class CommentDTO {

    private Integer commentId;

    private LocalDateTime addTime;

    private String content;

    private UserSimpleDTO user;

}

