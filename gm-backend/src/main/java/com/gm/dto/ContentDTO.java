package com.gm.dto;

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
public class ContentDTO {

    private Integer contentId;

    private LocalDateTime addTime;

    private String title;

    private String description;

    private String content;

    private String images;

    public List<String> getImageList() {
        List<String> result = new ArrayList<>();
        if(this.getImages() != null) {
            String[] imgs = this.getImages().split(",");
            result.addAll(Arrays.asList(imgs));
        }
        return result;
    }

    // Liked Count
    private Integer goodCount;

    //Collectd Count
    private Integer collectCount;

    // Status 1:Pass -1：Not Pass
    private Integer status;

    private UserSimpleDTO user;

    private CategoryDTO category;

    private ChannelsDTO channels;

    private ChannelsCategoryDTO channelsCategory;

    private List<CommentDTO> commentDTOS;

    // Whether the user is follow with the author of the content
    private Boolean collectUser = false;

    // Whether the user likes the content
    private Boolean collectLiked = false;

    // Whether the user collected the content
    private Boolean collectCollected = false;
}
