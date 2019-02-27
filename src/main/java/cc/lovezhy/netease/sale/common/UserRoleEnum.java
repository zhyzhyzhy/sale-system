package cc.lovezhy.netease.sale.common;

import cc.lovezhy.netease.sale.exception.HttpException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cc.lovezhy.netease.sale.exception.ResponseCodeEnum.INTERNAL_ERROR;

@AllArgsConstructor
@Getter
public enum UserRoleEnum {

    SELLER((byte) 0, "seller"),

    BUYER((byte) 1, "buyer");

    private byte code;

    private String desc;

    private static final Logger log = LoggerFactory.getLogger(UserRoleEnum.class);

    public static UserRoleEnum fromCode(byte roleCode) {
        for (UserRoleEnum role : values()) {
            if (role.code == roleCode) {
                return role;
            }
        }
        log.error("can not find role code ={}", roleCode);
        throw HttpException.create(INTERNAL_ERROR);
    }

}
