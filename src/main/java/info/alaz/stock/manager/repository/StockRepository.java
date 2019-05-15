package info.alaz.stock.manager.repository;

import info.alaz.stock.manager.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockRepository extends JpaRepository<StockEntity, UUID> {

    StockEntity findByIdAndProduct_Name(UUID stockUuid, String productName);

}
