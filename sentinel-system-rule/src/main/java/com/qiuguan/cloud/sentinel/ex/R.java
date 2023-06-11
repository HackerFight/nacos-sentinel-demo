package com.qiuguan.cloud.sentinel.ex;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qiuguan
 * @date 2023/05/23 22:30:44  星期二
 */
@Setter
@Getter
public class R<T> {

    private int code;

    private String msg;

    private T data;

    public static R<?> error(int code, String msg) {
        return new R<>(code, msg);
    }

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
