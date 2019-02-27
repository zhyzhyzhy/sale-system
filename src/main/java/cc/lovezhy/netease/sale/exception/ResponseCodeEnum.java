package cc.lovezhy.netease.sale.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    OK(200, "ok"),
    FAIL(300, "fail"),
    LOGIN_FAIL(401, "login fail"),
    NOT_LOGIN(402, "not login"),
    SESSION_INVALID(403, "session invalid")

    ;

    private int code;
    private String desc;
}
