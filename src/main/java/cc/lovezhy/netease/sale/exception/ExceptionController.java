package cc.lovezhy.netease.sale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResponse<?> handleException(Exception uncaughtException) {
        if (uncaughtException == null) {
            return HttpResponse.fail("No exception found.");
        }
        if (uncaughtException instanceof HttpException) {
            // 自定义异常
            HttpException httpException = (HttpException) uncaughtException;
            return HttpResponse.fail(httpException.getResponseCodeEnum());
        } else {
            return HttpResponse.fail(uncaughtException.getMessage());
        }
    }
}
