package com.example.admin.config.Result;

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
        return buildResult(ResultCode.SUCCESS, "成功", data);
    }

    public static Result success() {
        return buildResult(ResultCode.SUCCESS, "成功", null);
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
