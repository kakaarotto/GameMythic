package com.gm.vm;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class ContentVM extends BaseVM {

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

    // Status 1:Pass -1 Not pass
    private Integer status;

    private Integer userId;

    private Integer categoryId;

    private Integer channelsId;

    private Integer channelsCategoryId;

}
