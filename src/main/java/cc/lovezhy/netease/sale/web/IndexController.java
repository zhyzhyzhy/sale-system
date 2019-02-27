package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {


    private GoodService goodService;

    @Autowired
    public IndexController(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/")
    public String index(UserInfo userInfo, Model model) {
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("goods", goodService.listAll());
        return "index";
    }
}
