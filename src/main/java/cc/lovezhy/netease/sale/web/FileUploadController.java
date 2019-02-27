package cc.lovezhy.netease.sale.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @PostMapping("/pic")
    public ResponseEntity<?> uploadPic(@RequestParam MultipartFile pic) {


        return ResponseEntity.ok().build();
    }
}
