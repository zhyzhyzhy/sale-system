package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.model.RecordModel;
import cc.lovezhy.netease.sale.service.TransRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class SettleAccountController {


    private TransRecordService transRecordService;

    @Autowired
    public SettleAccountController(TransRecordService transRecordService) {
        this.transRecordService = transRecordService;
    }

    @GetMapping("/settleAccount")
    public String settleAccountPage() {
        return "settleAccount";
    }

    @GetMapping(path = {"/account.html", "/account"})
    public String account(UserInfo userInfo, Model model) {
        model.addAttribute("userInfo", userInfo);
        List<RecordModel> recordModels = transRecordService.queryAllRecord(userInfo);
        model.addAttribute("records", recordModels);
        BigDecimal bigDecimal = new BigDecimal(0);
        for (RecordModel recordModel : recordModels) {
            bigDecimal = bigDecimal.add(recordModel.getPrice().multiply(BigDecimal.valueOf(recordModel.getNumber())));
        }
        model.addAttribute("total", bigDecimal);
        return "account";
    }

}
