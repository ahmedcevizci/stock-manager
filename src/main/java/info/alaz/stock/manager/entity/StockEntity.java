package info.alaz.stock.manager.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
