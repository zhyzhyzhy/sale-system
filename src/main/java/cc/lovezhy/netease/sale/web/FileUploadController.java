package cc.lovezhy.netease.sale.web;

import cc.lovezhy.netease.sale.exception.HttpException;
import cc.lovezhy.netease.sale.exception.ResponseCodeEnum;
import cc.lovezhy.netease.sale.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/pic/upload")
    @ResponseBody
    public Map<String, String> uploadPic(@RequestParam MultipartFile pic) {
        try {
            System.out.println("ccccccccccccc");
            String url = storageService.upload(pic.getBytes(), pic.getOriginalFilename());
            return Collections.singletonMap("url", url);
        } catch (IOException e) {
            log.info(e.getMessage(), e);
            throw HttpException.create(ResponseCodeEnum.FAIL);
        }
    }
}
