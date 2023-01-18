package com.example.admin.config.exception;

import com.example.admin.config.Result.ResultCode;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private ResultCode resultCode;

    public ServiceException(ResultCode resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }

}
