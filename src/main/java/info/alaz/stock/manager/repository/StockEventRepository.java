package info.alaz.stock.manager.repository;

import info.alaz.stock.manager.entity.StockEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockEventRepository extends JpaRepository<StockEventEntity, UUID> {

}
