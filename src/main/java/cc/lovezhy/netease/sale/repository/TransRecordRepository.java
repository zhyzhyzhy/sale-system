package cc.lovezhy.netease.sale.repository;

import cc.lovezhy.netease.sale.entity.TransRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransRecordRepository extends JpaRepository<TransRecord, Integer> {
    List<TransRecord> findAllByUserId(Integer userId);
}
