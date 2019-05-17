package info.alaz.stock.manager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@DynamicUpdate
//Necessary for optimistic locking, solving concurrent update problem (dirty read)
@OptimisticLocking(type = OptimisticLockType.ALL)
@Entity
@Table(name = "tbl_stock")
public class StockEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fld_product_id", nullable = false)
    private ProductEntity product;

    @NotNull
    @Column(name = "fld_quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "fld_date_updated")
    private ZonedDateTime dateUpdated;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "dateUpdated DESC")
    private Set<StockEventEntity> stockEventEntitySet;

    public StockEntity(UUID id, ProductEntity product, Integer quantity, ZonedDateTime dateUpdated, Set<StockEventEntity> stockEventEntitySet) {
        super(id);
        this.product = product;
        this.quantity = quantity;
        this.dateUpdated = dateUpdated;
        this.stockEventEntitySet = stockEventEntitySet;
    }
}
