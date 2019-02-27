package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.common.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettleAccountController {

    @GetMapping("/settleAccount")
    public String settleAccountPage() {
        return "settleAccount";
    }

    @GetMapping(path = {"/account.html", "/account"})
    public String account(UserInfo userInfo, Model model) {
        model.addAttribute("userInfo", userInfo);
        return "account";
    }

}
