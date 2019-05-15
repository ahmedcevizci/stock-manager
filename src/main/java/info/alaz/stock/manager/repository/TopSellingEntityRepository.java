package info.alaz.stock.manager.repository;

import info.alaz.stock.manager.entity.TopSellingProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.Set;

public interface TopSellingEntityRepository extends JpaRepository<TopSellingProductEntity, String> {

    @Query(nativeQuery = true, name = "findTopSellingProductsQuery")
    Set<TopSellingProductEntity> findTopSellingProducts(ZonedDateTime afterDateChanged, int limit);
}
