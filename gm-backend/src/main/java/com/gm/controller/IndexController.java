package com.gm.controller;

import com.gm.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pujie
 */
@Controller
public class IndexController {

    /**
     * System backend index
     * @param request
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<?> home(HttpServletRequest request) {
        return ResponseUtil.renderSuccess("Game Mythic back-end system");
    }

}
