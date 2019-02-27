package cc.lovezhy.netease.sale.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRoleEnum {

    SELLER((byte) 0, "seller"),

    BUYER((byte) 1, "buyer");

    private byte code;

    private String desc;

}
