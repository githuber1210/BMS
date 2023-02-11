package com.example.admin.common.exception;

import com.example.admin.common.Result.ResultCode;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private ResultCode resultCode;

    public ServiceException(ResultCode resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }

}
