package info.alaz.stock.manager.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_stock_event")
public class StockEventEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fld_stock_id", nullable = false)
    private StockEntity stock;

    @NotNull
    @Column(name = "fld_quantity_change")
    private Integer quantityChange;

    @NotNull
    @Column(name = "fld_date_changed")
    private ZonedDateTime dateChanged;

}
