package cc.lovezhy.netease.sale.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SellerGoodModel {

    private Integer id;

    private String title;

    private String image;

    private BigDecimal price;

    private Boolean hasBeenSold;
}
