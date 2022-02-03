package com.file.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ResultEntity {

    public ResultEntity() {
    }

    public ResultEntity(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code = "00";
    public String msg = "成功";
    public Map itemMaps = new HashMap();


}
