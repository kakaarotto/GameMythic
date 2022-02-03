package com.file.api.controller;

import com.file.api.service.CurrencyService;
import com.file.api.dto.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author pujie
 */
@RestController
@RequestMapping("")
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

    @RequestMapping("/up-img")
    public ResultEntity upImg(@RequestParam MultipartFile[] file, String path){
        return currencyService.upImg(file, path);
    }

}
