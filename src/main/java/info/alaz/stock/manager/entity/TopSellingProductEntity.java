package info.alaz.stock.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedNativeQuery(name = "findTopSellingProductsQuery",
        resultClass = TopSellingProductEntity.class, resultSetMapping = "topSellingProductResultMapping",
        query = "WITH tbl_top_selling_stock AS" +
                " (SELECT ABS(SUM(fld_quantity_change)) AS sold_quantity, fld_stock_id FROM tbl_stock_event" +
                " WHERE fld_quantity_change < 0 AND fld_date_changed > ?1 " +
                " GROUP BY fld_stock_id ORDER BY sold_quantity DESC)" +
                " SELECT tss.fld_stock_id, s.fld_product_id, tss.sold_quantity FROM tbl_top_selling_stock tss, tbl_stock s" +
                " WHERE tss.fld_stock_id = s.fld_uuid LIMIT ?2")
@SqlResultSetMapping(
        name = "topSellingProductResultMapping",
        entities = @EntityResult(
                entityClass = TopSellingProductEntity.class,
                fields = {
                        @FieldResult(name = "stockId", column = "fld_stock_id"),
                        @FieldResult(name = "productName", column = "fld_product_id"),
                        @FieldResult(name = "itemsSold", column = "sold_quantity")}))
@Entity
public class TopSellingProductEntity {

    @Id
    @NotNull
    private UUID stockId;

    @NotNull
    private String productName;

    @NotNull
    @Min(0)
    private Integer itemsSold;
}
