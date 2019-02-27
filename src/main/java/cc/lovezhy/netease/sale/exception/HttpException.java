package cc.lovezhy.netease.sale.exception;

import lombok.Getter;

@Getter
public class HttpException extends RuntimeException {

    public static HttpException create(ResponseCodeEnum responseCodeEnum) {
        return new HttpException(responseCodeEnum);
    }

    private ResponseCodeEnum responseCodeEnum;

    public HttpException(ResponseCodeEnum responseCodeEnum) {
        this.responseCodeEnum = responseCodeEnum;
    }
}
