package cc.lovezhy.netease.sale.repository;

import cc.lovezhy.netease.sale.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRepository extends JpaRepository<Good, Integer> {
}
