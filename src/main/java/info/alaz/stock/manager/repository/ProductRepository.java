package info.alaz.stock.manager.repository;

import info.alaz.stock.manager.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

}
