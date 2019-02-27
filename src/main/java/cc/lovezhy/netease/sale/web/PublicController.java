package cc.lovezhy.netease.sale.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PublicController {

    private static final Logger log = LoggerFactory.getLogger(PublicController.class);

    @GetMapping("/public")
    public String publicGoodPage() {
        return "public";
    }

    @PostMapping(path = "/public", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addGood(@RequestParam Map<String, String> body) {
        return "publicSubmit";
    }
}
