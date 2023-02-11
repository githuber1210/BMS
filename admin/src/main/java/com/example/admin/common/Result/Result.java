package com.example.admin.common.Result;

import com.example.admin.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data) {
        return buildResult(ResultCode.SUCCESS, Constants.SUCCESS_MSG, data);
    }

    public static Result success() {
        return buildResult(ResultCode.SUCCESS, Constants.SUCCESS_MSG, null);
    }

    public static Result fail(ResultCode resultCode,String msg) {
        return buildResult(resultCode, msg, null);
    }

    public static Result buildResult(ResultCode resultCode, String msg, Object data) {
        return buildResult(resultCode.code, msg, data);
    }

    public static Result buildResult(int resultCode, String msg, Object data) {
        return new Result(resultCode, msg, data);
    }

}
