package cc.lovezhy.netease.sale.config;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.exception.HttpException;
import cc.lovezhy.netease.sale.service.RedisService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cc.lovezhy.netease.sale.exception.ResponseCodeEnum.NOT_LOGIN;
import static cc.lovezhy.netease.sale.exception.ResponseCodeEnum.SESSION_INVALID;


@Component
public class UserInfoValidationConfigurer implements InitializingBean {

    private static final String COOKIE_KEY = "session_id";

    private RedisService redisService;

    @Value("${auth.excludeUrl}")
    private String excludeUrl;

    private List<String> excludeUrls;

    public static ThreadLocal<UserInfo> userInfoHolder = new ThreadLocal<>();

    @Autowired
    public UserInfoValidationConfigurer(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        excludeUrls = Arrays.asList(excludeUrl.split(","));
    }

    @Bean
    public HandlerInterceptor authenticationInterceptor() {
        return new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, ServletException {
                String requestURI = request.getRequestURI();
                Cookie cookie = getCookie(request, COOKIE_KEY);
                if (cookie == null) {
                    if (excludeUrls.contains(requestURI)) {
                        userInfoHolder.set(UserInfo.create(Boolean.FALSE));
                    } else {
                        throw HttpException.create(NOT_LOGIN);
                    }
                } else {
                    String authToken = cookie.getValue();
                    UserInfo userInfo = redisService.getFromCache(authToken, UserInfo.class);
                    if (userInfo != null) {
                        redisService.expandExpireTime(authToken, 180, TimeUnit.DAYS);
                        userInfoHolder.set(userInfo);
                    } else if (excludeUrls.contains(requestURI)) {
                        userInfoHolder.set(UserInfo.create(Boolean.FALSE));
                    } else {
                        throw HttpException.create(SESSION_INVALID);
                    }
                }
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                userInfoHolder.remove();
            }
        };
    }

    @Bean
    public HandlerMethodArgumentResolver userInfoArgumentResolver() {
        return new HandlerMethodArgumentResolver() {

            ///判断是否支持要转换的参数类型,这里默认全部支持
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return true;
            }

            //当支持后进行相应的转换
            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return userInfoHolder.get();
            }
        };
    }

    private static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
