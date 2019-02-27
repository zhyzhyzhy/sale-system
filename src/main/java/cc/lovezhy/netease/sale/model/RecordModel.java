package cc.lovezhy.netease.sale.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RecordModel {

    private Integer id;

    private String image;

    private String title;

    private Date createTime;

    private Integer number;

    private BigDecimal price;
}
