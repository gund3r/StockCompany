package documents.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Introspected
public class SaleGoodsDTO {
    @NotNull
    private int documentNumber;
    @NotNull
    private long stockIdFrom;
    @NotNull
    @NotEmpty
    private List<GoodAmount> saleGoods;

    public SaleGoodsDTO() {}

    public SaleGoodsDTO(int documentNumber, long stockIdFrom, List<GoodAmount> saleGoods) {
        this.documentNumber = documentNumber;
        this.stockIdFrom = stockIdFrom;
        this.saleGoods = saleGoods;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public long getStockIdFrom() {
        return stockIdFrom;
    }

    public void setStockIdFrom(long stockIdFrom) {
        this.stockIdFrom = stockIdFrom;
    }

    public List<GoodAmount> getSaleGoods() {
        return saleGoods;
    }

    public void setSaleGoods(List<GoodAmount> saleGoods) {
        this.saleGoods = saleGoods;
    }

    @Override
    public String toString() {
        return "SaleGoodsDTO{" +
                "documentNumber=" + documentNumber +
                ", stockIdFrom=" + stockIdFrom +
                ", saleGoods=" + saleGoods +
                '}';
    }
}
