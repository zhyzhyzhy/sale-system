package cc.lovezhy.netease.sale.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodModel {

    private String title;

    private String summary;

    private String image;

    private String detail;

    private BigDecimal price;
}
