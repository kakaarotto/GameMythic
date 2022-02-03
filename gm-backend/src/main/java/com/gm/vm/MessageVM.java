package com.gm.vm;

import com.gm.dto.UserSimpleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageVM extends BaseVM {

    //Type: 1 followed，2 liked，3 commented，4 content
    private Integer type;

    private Integer fromUserId;

    private Integer fromTypeId;

    private String title;

    private String content;

    private LocalDateTime addTime;

    private Integer userId;

    private Integer readState;

    private LocalDateTime readTime;
}
