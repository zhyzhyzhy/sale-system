package cc.lovezhy.netease.sale;

import cc.lovezhy.netease.sale.common.UserRoleEnum;
import cc.lovezhy.netease.sale.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleApplication.class, args);
    }

    private UserService userService;

    @Autowired
    public SaleApplication(UserService userService) {
        this.userService = userService;
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            userService.insertUser("seller", DigestUtils.md5Hex("relles".getBytes()), UserRoleEnum.SELLER);
            userService.insertUser("buyer", DigestUtils.md5Hex("reyub".getBytes()), UserRoleEnum.BUYER);
        };
    }

}

