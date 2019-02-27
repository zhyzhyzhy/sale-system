package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.model.GoodModel;
import cc.lovezhy.netease.sale.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoodController {


    private GoodService goodService;

    @Autowired
    public GoodController(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/show")
    public String showGoodPage(UserInfo userInfo,
                               @RequestParam Integer id,
                               Model model) {
        model.addAttribute("userInfo", userInfo);
        GoodModel goodModel = goodService.queryGoodModel(userInfo, id);
        model.addAttribute("good", goodModel);
        return "show";
    }

    @GetMapping("/edit")
    public String editGoodPage(UserInfo userInfo,
                               @RequestParam Integer id,
                               Model model) {
        model.addAttribute("userInfo", userInfo);
        GoodModel goodModel = goodService.queryGoodModel(userInfo, id);
        model.addAttribute("good", goodModel);
        return "edit";
    }
}
