package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {


    private AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "loginin";
    }

    @PostMapping("/login")
    @ResponseBody
    public Boolean userLogin(@RequestParam String userName,
                             @RequestParam String password,
                             HttpServletResponse httpServletResponse) {
        authService.login(userName, password, httpServletResponse);
        return true;
    }
}
