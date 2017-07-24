package com.tq.simplybook.resp;

import java.io.Serializable;
import java.util.List;

public class SbmError implements Serializable{
    private static final long serialVersionUID = 6526222623256710399L;
    private String code;
    private String message;
    private List<?> data = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SbmError [code=" + code + ", message=" + message + ", data=" + data + "]";
    }
}
