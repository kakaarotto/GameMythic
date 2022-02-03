package com.gm.dto;

import com.gm.model.Collect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Integer messageId;

    // Type: 1 followed，2 liked，3 commented，4 content
    private Integer type;

    private Integer fromUserId;
    private Integer fromTypeId;
    private UserDTO fromUser;

    private String title;

    private String content;

    private LocalDateTime addTime;

    private UserSimpleDTO user;

    private Integer readState;

    private LocalDateTime readTime;

    private String fromCommentContent;
    private ContentDTO fromContent;
}
