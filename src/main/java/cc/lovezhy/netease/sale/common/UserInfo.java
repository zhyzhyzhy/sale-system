package cc.lovezhy.netease.sale.common;

import lombok.Data;

@Data
public class UserInfo {
    public static UserInfo create(boolean valid) {
        UserInfo userInfo = new UserInfo();
        userInfo.setValid(valid);
        return userInfo;
    }

    private Boolean valid;

    private String sessionId;

    private Integer userId;

    private String name;

    private UserRoleEnum userRole;

    public boolean isSeller() {
        return valid && userRole.equals(UserRoleEnum.SELLER);
    }

    public boolean isBuyer() {
        return valid && userRole.equals(UserRoleEnum.BUYER);
    }
}
