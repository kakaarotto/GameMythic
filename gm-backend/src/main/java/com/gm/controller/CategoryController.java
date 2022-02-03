package com.gm.controller;

import com.gm.service.CategoryService;
import com.gm.dto.CategoryDTO;
import com.gm.service.mapper.CategoryMapper;
import com.gm.query.CategoryQuery;
import com.gm.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author pujie
 */
@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    /**
     * Get category list
     * @param pageable
     * @param query
     * @return
     */
    @GetMapping("/categories")
    public ResponseEntity<?> getDatas(@PageableDefault(sort = {"addTime"}, direction = Sort.Direction.DESC) Pageable pageable, CategoryQuery query) {
        Page<CategoryDTO> page = categoryService.search(query, pageable).map(object -> CategoryMapper.INSTANCE.modelToDTO(object));
        return ResponseUtil.renderSuccess(page);
    }

}
