package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {


    private GoodService goodService;

    @Autowired
    public IndexController(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/")
    public String index(UserInfo userInfo,
                        @RequestParam(required = false, defaultValue = "0") Integer type,
                        Model model) {
        model.addAttribute("userInfo", userInfo);
        if (userInfo.isSeller()) {
            model.addAttribute("goods", goodService.sellerListAll(userInfo));
        } else {
            model.addAttribute("goods", goodService.list(userInfo, type));
        }
        model.addAttribute("type", type);
        return "index";
    }
}
