package info.alaz.stock.manager.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_product")
public class ProductEntity {

    @Id
    @Column(name = "fld_name")
    private String name;

    @NotNull
    @Column(name = "fld_date_created")
    private ZonedDateTime dateCreated;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "dateUpdated DESC")
    private Set<StockEntity> stockEntitySet;

}
