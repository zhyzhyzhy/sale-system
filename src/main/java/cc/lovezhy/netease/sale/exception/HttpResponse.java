package cc.lovezhy.netease.sale.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import static cc.lovezhy.netease.sale.exception.ResponseCodeEnum.FAIL;
import static cc.lovezhy.netease.sale.exception.ResponseCodeEnum.OK;

@Data
@AllArgsConstructor
public class HttpResponse<T> {

    private boolean success;

    private int code;

    private String message;

    private T result;

    public static <T> HttpResponse<T> success(T result) {
        return new HttpResponse<T>(true, OK.getCode(), OK.getDesc(), result);
    }

    public static HttpResponse<?> fail(String message) {
        return new HttpResponse<>(false, FAIL.getCode(), message, null);
    }

    public static HttpResponse<?> fail(ResponseCodeEnum responseCodeEnum) {
        return new HttpResponse<>(false, responseCodeEnum.getCode(), responseCodeEnum.getDesc(), null);
    }

}
