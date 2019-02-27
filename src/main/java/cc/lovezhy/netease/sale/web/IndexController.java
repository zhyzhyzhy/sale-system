package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.common.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(UserInfo userInfo, Model model) {
        model.addAttribute("userInfo", userInfo);
        return "index";
    }
}
