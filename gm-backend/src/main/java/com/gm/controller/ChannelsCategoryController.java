package com.gm.controller;

import com.gm.service.ChannelsCategoryService;
import com.gm.dto.ChannelsCategoryDTO;
import com.gm.service.mapper.ChannelsCategoryMapper;
import com.gm.query.ChannelsCategoryQuery;
import com.gm.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pujie
 */
@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class ChannelsCategoryController extends BaseController {

    private final ChannelsCategoryService channelsCategoryService;

    /**
     * Get All channel categories
     * @param pageable
     * @param query
     * @return
     */
    @GetMapping("/channels-categories")
    public ResponseEntity<?> getChannelCid(@PageableDefault(sort = {"addTime"}, direction = Sort.Direction.DESC) Pageable pageable, ChannelsCategoryQuery query) {
        Page<ChannelsCategoryDTO> page = channelsCategoryService.search(query, pageable).map(object -> ChannelsCategoryMapper.INSTANCE.modelToDTO(object));
        return ResponseUtil.renderSuccess(page);
    }

}
