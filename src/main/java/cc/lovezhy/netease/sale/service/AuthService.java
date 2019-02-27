package cc.lovezhy.netease.sale.service;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.entity.User;
import cc.lovezhy.netease.sale.exception.HttpException;
import cc.lovezhy.netease.sale.exception.ResponseCodeEnum;
import cc.lovezhy.netease.sale.util.IdGenerator;
import com.alibaba.fastjson.JSON;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
public class AuthService {

    private UserService userService;

    private RedisService redisService;

    private static final String COOKIE_KEY = "session_id";

    @Autowired
    public AuthService(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    public boolean login(String userName, String password, HttpServletResponse httpServletResponse) {
        User user = userService.findByName(userName);
        if (Objects.isNull(user) || !BCrypt.checkpw(password, user.getPassword())) {
            throw HttpException.create(ResponseCodeEnum.LOGIN_FAIL);
        }
        String sessionId = IdGenerator.newId();
        httpServletResponse.addCookie(new Cookie(COOKIE_KEY, sessionId));

        UserInfo userInfo = UserInfo.create(true);
        userInfo.setName(user.getName());
        userInfo.setSessionId(sessionId);
        redisService.setToCacheAsync(sessionId, JSON.toJSONString(userInfo));
        return true;
    }

    public boolean logout(UserInfo userInfo) {
        if (userInfo.getValid()) {
            redisService.removeFromCache(userInfo.getSessionId());
        }
        return true;
    }
}
