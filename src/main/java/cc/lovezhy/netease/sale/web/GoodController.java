package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.model.BuyingGoodModel;
import cc.lovezhy.netease.sale.model.GoodModel;
import cc.lovezhy.netease.sale.service.GoodService;
import cc.lovezhy.netease.sale.service.TransRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodController {

    private GoodService goodService;
    private TransRecordService transRecordService;

    @Autowired
    public GoodController(GoodService goodService, TransRecordService transRecordService) {
        this.goodService = goodService;
        this.transRecordService = transRecordService;
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

    @PostMapping("/buy")
    @ResponseBody
    public Boolean buyGoods(UserInfo userInfo,
                            @RequestBody List<BuyingGoodModel> buyingGoodModelList) {

        transRecordService.insert(userInfo, buyingGoodModelList);
        return true;
    }
}
