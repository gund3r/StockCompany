package documents;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotNull;

@MappedEntity
public class SaleGoodsDocument {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private Long id;
    @NotNull
    private int documentNumber;
    @NotNull
    private long stockIdFrom;
    @NotNull
    private long saleGoodsId;
    @NotNull
    private int saleGoodsAmount;
    @NotNull
    private int price;

    public SaleGoodsDocument(int documentNumber, long stockIdFrom, long saleGoodsId, int saleGoodsAmount, int price) {
        this.documentNumber = documentNumber;
        this.stockIdFrom = stockIdFrom;
        this.saleGoodsId = saleGoodsId;
        this.saleGoodsAmount = saleGoodsAmount;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public long getSaleGoodsId() {
        return saleGoodsId;
    }

    public void setSaleGoodsId(long saleGoodsId) {
        this.saleGoodsId = saleGoodsId;
    }

    public int getSaleGoodsAmount() {
        return saleGoodsAmount;
    }

    public void setSaleGoodsAmount(int saleGoodsAmount) {
        this.saleGoodsAmount = saleGoodsAmount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SaleGoodsDocument{" +
                "id=" + id +
                ", documentNumber=" + documentNumber +
                ", stockIdFrom=" + stockIdFrom +
                ", saleGoodsId=" + saleGoodsId +
                ", saleGoodsAmount=" + saleGoodsAmount +
                ", price=" + price +
                '}';
    }
}
