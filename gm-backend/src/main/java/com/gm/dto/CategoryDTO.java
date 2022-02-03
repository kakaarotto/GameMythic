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
public class CategoryDTO {

    private Integer categoryId;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    // Category name
    private String name;

}
