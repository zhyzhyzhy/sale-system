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

    private String name;

    private UserRoleEnum userRole;


}
