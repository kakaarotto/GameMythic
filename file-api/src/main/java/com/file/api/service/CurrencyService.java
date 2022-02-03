package com.file.api.service;

import com.file.api.component.exception.CoreException;
import com.file.api.component.util.Common;
import com.file.api.dto.ResultEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * @author pujie
 */
public interface CurrencyService {

    /**
     * upload image
     * @param file
     * @param path
     * @return image url
     */
    ResultEntity upImg(@RequestParam MultipartFile[] file, String path);
}
