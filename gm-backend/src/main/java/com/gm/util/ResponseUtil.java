package com.gm.util;

import com.gm.dto.ResponseDTO;
import com.gm.exception.ExceptionResponse;
import com.gm.exception.ServiceVerifyException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Utility class for ResponseEntity creation.
 */
public interface ResponseUtil {

    /**
     * Return success (200 list/page collection)
     *
     * @param <X>
     * @return
     */
    static <X> ResponseEntity<?> renderSuccess(X value) {
        if (value == null) {
            return new ResponseEntity<X>((X) new ResponseDTO(0, "No data"), HttpStatus.OK);
        }
        if (value instanceof Page) {
            Page pageValue = (Page) value;
            if (pageValue.getContent().isEmpty()) {
                return new ResponseEntity<X>((X) new ResponseDTO(0, "No data"), HttpStatus.OK);
            }
            StringBuffer sbf = new StringBuffer();
            sbf.append("{\"code\":" + 1 + ",");
            sbf.append("\"message\":\"Get success\",");
            sbf.append("\"content\":" + JacksonUtil.toJSON(pageValue.getContent()) + ",");
            sbf.append("\"number\":" + pageValue.getNumber() + ",");
            sbf.append("\"size\":" + pageValue.getSize() + ",");
            sbf.append("\"numberOfElements\":" + pageValue.getNumberOfElements() + ",");
            sbf.append("\"totalElements\":" + pageValue.getTotalElements() + ",");
            sbf.append("\"totalPages\":" + pageValue.getTotalPages() + ",");
            sbf.append("\"first\":" + pageValue.isFirst() + ",");
            sbf.append("\"last\":" + pageValue.isLast() + ",");
            sbf.append("\"sort\":" + JacksonUtil.toJSON(pageValue.getSort()));
            sbf.append("}");
            return ResponseEntity.ok().body(sbf.toString());
        } else if (value instanceof List) {
            List listValue = (List) value;
            if (listValue.isEmpty()) {
                return new ResponseEntity<X>((X) new ResponseDTO(0, "No data"), HttpStatus.OK);
            }
            String response = "{\"code\":1,\"message\":\"Get success\",\"" + "content" + "\":" + JacksonUtil.toJSON(value) + "}";
            return ResponseEntity.ok().body(response);
        }
        return new ResponseEntity<X>(value, HttpStatus.OK);
    }

    /**
     * Return success (200 code+msg)
     *
     * @param <X>
     * @return
     */
    static <X> ResponseEntity<?> renderSuccess(String msg) {
        return new ResponseEntity<>(new ExceptionResponse(1, msg), HttpStatus.OK);

    }

    /**
     * Return success (200 code+msg+content object)
     *
     * @param msg
     * @param object
     * @return
     */
    static <X> ResponseEntity<?> renderSuccess(String msg, Object object) {
        String response = "{\"code\":1,\"message\":\"" + msg + "\",\"" + "content" + "\":" + JacksonUtil.toJSON(object) + "}";
        return ResponseEntity.ok().body(response);
    }

    /**
     * Return failure (400 code+message)
     *
     * @param msg
     * @return
     */
    static <X> ResponseEntity<?> renderError(String msg) {
        return ResponseEntity.ok().body(new ExceptionResponse(-1, msg));
    }

    /**
     * Return failure (400 code+message)
     *
     * @param e
     */
    static <X> ResponseEntity<?> renderError(ServiceVerifyException e) {
        return ResponseEntity.badRequest().body(new ServiceVerifyException(e.getCode(), e.getMessage()));
    }

    /**
     * Return success (200 code+msg+content+object)
     *
     * @param msg
     * @param object
     * @return
     */
    static <X> ResponseEntity<?> renderError(String msg, Object object) {
        String response = "{\"code\":-1,\"message\":\"" + msg + "\",\"" + "content" + "\":" + JacksonUtil.toJSON(object) + "}";
        return ResponseEntity.ok().body(response);

    }

    /**
     * Return failure (400 code+message)
     *
     * @param msg
     * @return
     */
    static <X> ResponseEntity<?> renderError(Integer code, String msg) {
        return ResponseEntity.ok().body(new ResponseDTO(code, msg));
    }

    /**
     * Return failure (400 code+message+object)
     *
     * @param msg
     * @return
     */
    static <X> ResponseEntity<?> renderError(Integer code, String msg, Object data) {
        String response = "{\"code\": " + code + ",\"message\":\"" + msg + "\",\"" + "content" + "\":" + JacksonUtil.toJSON(data) + "}";
        return ResponseEntity.ok().body(response);
    }

    /**
     * Return no data (200 code+message)
     *
     * @return
     */
    static <X> ResponseEntity<?> renderNoData() {
        return new ResponseEntity<>(new ExceptionResponse(0, "暂无数据"), HttpStatus.OK);
    }

    /**
     * Access token expiration prompt
     */
    static <X> ResponseEntity<?> expires() {
        return ResponseEntity.badRequest().body(new ExceptionResponse(-9, "User token expired, need to login again"));
    }


}