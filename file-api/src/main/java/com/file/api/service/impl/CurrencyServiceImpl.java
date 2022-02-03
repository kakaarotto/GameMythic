package com.file.api.service.impl;

import com.file.api.component.exception.CoreException;
import com.file.api.component.util.Common;
import com.file.api.dto.ResultEntity;
import com.file.api.service.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author pujie
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Override
    public ResultEntity upImg(@RequestParam MultipartFile[] file, String path) {
        ResultEntity resultEntity = new ResultEntity();

        // Iterate all uploaded pictures
        for (int i = 0; i < file.length; i++) {
            String fileName = file[i].getOriginalFilename();
            String suffixName;
            if (fileName.equals("blob")) {
                suffixName = ".png";
            } else {
                suffixName = fileName.substring(fileName.lastIndexOf("."));
            }
            // The front end specifies the upload path
            String filePath = Common.IMAGE_PATH + path;
            // Generated file name
            fileName = "WTJ" + UUID.randomUUID() + suffixName;
            File dest = new File(filePath + fileName);

            // Determine whether the folder of the upload path exists, mkdirs creates a new folder
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            // Save the uploaded picture data in the specified file
            try {
                file[i].transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
                throw new CoreException("Save failed");
            }
            // Return image URL
            resultEntity.itemMaps.put("imgUrl", Common.IMAGE_URL + path + fileName);
        }
        return resultEntity;
    }
}
