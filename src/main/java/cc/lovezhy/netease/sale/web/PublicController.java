package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.entity.Good;
import cc.lovezhy.netease.sale.service.GoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

@Controller
public class PublicController {

    private static final Logger log = LoggerFactory.getLogger(PublicController.class);

    private GoodService goodService;

    @Autowired
    public PublicController(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/public")
    public String publicGoodPage() {
        return "public";
    }

    @PostMapping(path = "/public", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addGood(UserInfo userInfo, @RequestParam Map<String, String> body, Model model) {
        Good good = new Good();
        good.setTitle(body.get("title"));
        good.setSummary(body.get("summary"));
        good.setImage(body.get("image"));
        good.setPrice(new BigDecimal(body.get("price")));
        good.setDetail(body.get("detail"));
        goodService.insert(userInfo, good);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("id", good.getId());
        return "publicSubmit";
    }

    @PostMapping(path = "/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editGood(UserInfo userInfo, @RequestParam Map<String, String> body, Model model) {

        Good good = new Good();
        good.setId(Integer.parseInt(body.get("id")));
        good.setTitle(body.get("title"));
        good.setSummary(body.get("summary"));
        good.setImage(body.get("image"));
        good.setPrice(new BigDecimal(body.get("price")));
        good.setDetail(body.get("detail"));
        good.setUserId(userInfo.getUserId());
        goodService.update(userInfo, good);

        model.addAttribute("userInfo", userInfo);
        model.addAttribute("id", good.getId());
        return "editSubmit";
    }
}
