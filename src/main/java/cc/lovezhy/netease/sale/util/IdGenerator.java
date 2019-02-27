package cc.lovezhy.netease.sale.util;

import java.util.UUID;

public class IdGenerator {
    public static String newId() {
        return UUID.randomUUID().toString();
    }
}
